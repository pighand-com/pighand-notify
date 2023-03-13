import com.pighand.notify.Application;
import com.pighand.notify.service.sender.MessageSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(classes = Application.class)
@DisplayName("模板参数测试用例")
public class TemplateParamsTest {

    @Autowired MessageSender messageSender;

    @DisplayName("测试{CODE}")
    @Test
    void testCode() {
        String template = "这是一个随机验证码：{CODE}";
        String result = messageSender.replaceTemplateParams(template);
        assertNotEquals(template, result);
    }

    @DisplayName("测试指定长度CODE：{CODE_num}")
    @Test
    void testCodeNum() {
        String template = "这是一个随机验证码：{CODE_4}";
        String result = messageSender.replaceTemplateParams(template);
        assertNotEquals(template, result);
    }
}
