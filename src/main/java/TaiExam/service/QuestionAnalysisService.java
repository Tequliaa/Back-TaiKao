package TaiExam.service;

import TaiExam.model.vo.QuestionAnalysisVO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

public interface QuestionAnalysisService {
    void getAnalysisDataStream(List<QuestionAnalysisVO> questionAnalysisVOList, SseEmitter emitter) throws IOException;
}
