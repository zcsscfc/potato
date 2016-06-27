import com.potato.api.framework.util.Base64Util;
import org.junit.Test;

/**
 * Created by zhangcs on 2016/6/3.
 */
public class ToolTest {
    @Test
    public void base64Test(){
        String originValue="065945";
        String base64Value= Base64Util.encode(originValue);
        System.out.println(base64Value);

        String newValue=Base64Util.decode(base64Value);
        System.out.println(newValue);
    }
}
