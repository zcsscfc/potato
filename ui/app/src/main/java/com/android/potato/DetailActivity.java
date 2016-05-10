package com.android.potato;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by s1112001 on 2016/5/10.
 */
public class DetailActivity extends Activity {
    private TextView tv_title,tv_source_date;
    private WebView wv_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageButton img_btn_cancel = (ImageButton)findViewById(R.id.img_btn_cancel);
        img_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_source_date = (TextView) findViewById(R.id.tv_source_date);
        wv_content = (WebView) findViewById(R.id.wv_content);

        tv_title.setText("发明专利：新疆理化所栽培出食用翘鳞环锈伞菌种");
        Date _date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        String str_date = sdf.format(_date);
        tv_source_date.setText("中国农业技术网   "+str_date);
        String content = "<html>\\n <head>\\n </head>\\n <body>\\n  <div class=\\\"zxxw\\\">\\n   <p class=\\\"zxxw1\\\">\\n    青饲料收获机常见故障的解决\\n   </p>\\n   <p class=\\\"zxxw2\\\">\\n    <span>\\n     <a href=\\\"#pinglun\\\">\\n      <img align=\\\"absmiddle\\\" alt=\\\"评论\\\" src=\\\"http://www.zhuwang.cc/templets/default/images/zxwz_03.jpg\\\"/>\\n      <b>\\n      </b>\\n     </a>\\n    </span>\\n    来源：  2016-05-04|  查看：\\n    <script src=\\\"http://www.zhuwang.cc/plus/count.php?view=yes&amp;aid=264653&amp;mid=21595\\\" type=\\\"text/javascript\\\">\\n    </script>\\n    次\\n   </p>\\n   <p class=\\\"zxxw-nr\\\" style=\\\"margin-bottom:15px;\\\">\\n    <script language=\\\"javascript\\\" src=\\\"http://www.zhuwang.cc/plus/ad_js.php?aid=39\\\">\\n    </script>\\n   </p>\\n   <p>\\n   </p>\\n   <p style=\\\"text-indent:2em;\\\">\\n    <span style=\\\"font-family:SimSun;font-size:16px;line-height:2;\\\">\\n     青\\n     <a href=\\\"http://www.zhuwang.cc/siliao/\\\">\\n      <u>\\n       饲料\\n      </u>\\n     </a>\\n     收获机常见故障有以下几个：\\n    </span>\\n   </p>\\n   <p style=\\\"text-indent:2em;\\\">\\n    <span style=\\\"font-family:SimSun;font-size:16px;line-height:2;\\\">\\n     1、切割器故障。主要表现是割草不利或漏割，原因是割刀磨损或缺少刀片，应及时更换刀片，定动刀片间隙越小越好，以不相碰为原则，间隙应在0.2~0.4毫米，并检查刀杆传动机构零部件，保证运转正常。\\n    </span>\\n   </p>\\n   <p style=\\\"text-indent:2em;\\\">\\n    <span style=\\\"font-family:SimSun;font-size:16px;line-height:2;\\\">\\n     2、拨禾轮故障。拨禾轮故障主要在传动和调整上，应保证拨禾轮传动正常，拨禾轮调整主要有高度调整，可根据作物的高低用升降油缸来掌握；拨禾轮水平调整，可通过调节板上的孔来调整，最前面的孔适于高秆或倒伏作业；拨禾轮的转速调整要根据作物的长势或机器前进速度而改变，可调换链轮来选择。\\n    </span>\\n   </p>\\n   <p style=\\\"text-indent:2em;\\\">\\n    <span style=\\\"font-family:SimSun;font-size:16px;line-height:2;\\\">\\n     3、收割台不稳故障。收割台不稳主要原因是分配器或油缸漏油而致，应更换油封“O”型圈来排除。\\n    </span>\\n   </p>\\n   <p style=\\\"text-indent:2em;\\\">\\n    <span style=\\\"font-family:SimSun;font-size:16px;line-height:2;\\\">\\n     4、搅轮、链耙故障。此处故障主要原因是安全离合器打滑，打滑原因可能是安全离合器进油或扭矩不足，应清洗油污并按规定扭矩压紧。\\n    </span>\\n   </p>\\n   <p style=\\\"text-indent:2em;\\\">\\n    <span style=\\\"font-family:SimSun;font-size:16px;line-height:2;\\\">\\n     5、切碎部分故障。切碎长度调整，可用增减切碎刀片数量或喂入的转速来调整；切碎部分故障主要有动、定刀损坏，轴承损坏等。\\n    </span>\\n   </p>\\n   <p style=\\\"text-indent:2em;\\\">\\n    <span style=\\\"font-family:SimSun;font-size:16px;line-height:2;\\\">\\n     6、整机抖动和抛料不畅故障。整机抖动的原因是切碎滚筒轴两边轴承损坏或切碎刀损坏，应停车检查，更换损坏或磨损的轴承，并检查定刀片损坏的情况，及时更换成对的新刀片，保持动刀的运转平稳；抛料不畅的原因主要是抛料筒内有阻碍物或风力不足，应更换风板，排除抛料筒内的倒刺等阻碍物。\\n    </span>\\n   </p>\\n   <p>\\n   </p>\\n   <div class=\\\"pages\\\">\\n    <ul class=\\\"pagelist\\\">\\n    </ul>\\n   </div>\\n   <!-- /pages -->\\n  </div>\\n </body>\\n</html>";
        content = content.replace("\\n","");
        wv_content.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
    }
}
