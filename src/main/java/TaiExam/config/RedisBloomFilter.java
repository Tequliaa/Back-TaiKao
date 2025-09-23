package TaiExam.config;

import TaiExam.mapper.ExamMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

/**
 * 基于Redis的布隆过滤器（防缓存击穿）
 * 用于快速判断ExamId是否存在，减少对不存在ID的数据库查询
 */
@Component
@Slf4j
public class RedisBloomFilter {
    @Resource
    private ExamMapper examMapper;

    // 布隆过滤器在Redis中的Key
    private static final String BLOOM_FILTER_KEY = "bloom:filter:Exam:ids";

    // 预计存储的问卷ID数量（根据业务估算，这里设10万）
    private static final long EXPECTED_INSERTIONS = 100000;

    // 可接受的误判率（越小需要的bit越多，这里设0.01=1%）
    private static final double FALSE_POSITIVE_RATE = 0.01;

    // 布隆过滤器需要的bit总数
    private final long numBits;

    // 需要的哈希函数数量
    private final int numHashFunctions;

    // 操作BitMap（如布隆过滤器）：用专用bitmapRedisTemplate
    @Resource(name = "bitmapRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    public RedisBloomFilter() {
        // 计算bit总数（公式：n * log(p) / (log(2)^2)，n是预计数量，p是误判率）
        this.numBits = (long) (-EXPECTED_INSERTIONS * Math.log(FALSE_POSITIVE_RATE) / (Math.log(2) * Math.log(2)));
        // 计算哈希函数数量（公式：(nBits / n) * log(2)）
        this.numHashFunctions = Math.max(1, (int) Math.round((double) numBits / EXPECTED_INSERTIONS * Math.log(2)));
    }

    /**
     * 初始化：项目启动时将数据库中已有的ExamId加载到布隆过滤器
     * （实际场景可异步执行，避免启动阻塞）
     */
    @PostConstruct
    public void init() {
         List<Integer> allExamIds = examMapper.getAllExamIds();
         log.info("allExamIds:{}",allExamIds);
         allExamIds.forEach(this::add);
    }

    /**
     * 添加ExamId到布隆过滤器
     */
    public void add(int ExamId) {
        byte[] bytes = String.valueOf(ExamId).getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < numHashFunctions; i++) {
            long index = hash(bytes, i);
            // 向Redis的bitMap中设置bit（offset=index，value=true）
            redisTemplate.opsForValue().setBit(BLOOM_FILTER_KEY, index, true);
        }
    }

    /**
     * 判断ExamId是否可能存在（存在返回true，一定不存在返回false）
     */
    public boolean mightContain(int ExamId) {
        byte[] bytes = String.valueOf(ExamId).getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < numHashFunctions; i++) {
            long index = hash(bytes, i);
            // 只要有一个bit为false，说明一定不存在
            if (!redisTemplate.opsForValue().getBit(BLOOM_FILTER_KEY, index)) {
                return false;
            }
        }
        // 所有bit都为true，可能存在（有一定误判率）
        return true;
    }
    /**
     * 哈希函数（生成多个不同的索引）
     */
    private long hash(byte[] bytes, int i) {
        Random random = new Random(i); // 用i作为种子，保证不同哈希函数生成不同值
        BitSet bitSet = BitSet.valueOf(bytes);
        random.setSeed(bitSet.toLongArray().length > 0 ? bitSet.toLongArray()[0] : 0);
        return Math.abs(random.nextLong() % numBits);
    }

}
