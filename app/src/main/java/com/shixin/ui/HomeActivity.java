package com.shixin.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hi.dhl.paging3.network.ui.MainPagingActivity;
import com.shixin.R;
import com.shixin.apt_annotation.AptAnnotation;
import com.shixin.bean.Bird;
import com.shixin.ui.activityrecord.TestActivitySingleTaskActvity;
import com.shixin.ui.customview.CustomViewGuideActivity;
import com.shixin.ui.jetpack.JetIndexActivity;
import com.shixin.ui.jetpack.databinding.MainActivity;
import com.shixin.ui.ndktest.NdkTestActivity;
import com.shixin.ui.practice.PracticeGuideActivity;
import com.shixin.ui.sourceread.ReadSourceActivity;
import com.shixin.ui.sourceread.TestMeasureSpecActivity;
import com.shixin.ui.thread.ThreadActivity;
import com.shixin.ui.windowmanager.WindowManagerDemoActivity;
import com.shixin.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

@AptAnnotation(desc = "我是 MainActivity 上面的注解")
public class HomeActivity extends AppCompatActivity {

    String str = "[[[120.27677694815127,31.573361540042004],[120.2768272646921,31.57342964714268],[120.27687759861199,31.57351475960856],[120.27690375924068,31.573546294187434],[120.27692791309207,31.57358086628481],[120.27697219701328,31.573646082427313],[120.2770184235451,31.573645241731917],[120.27701844086882,31.573662246982476],[120.27702850846143,31.573680069796918],[120.27702856139487,31.573732085839286],[120.27702860918994,31.573779100323577],[120.27703364499895,31.573790012342247],[120.27703367750631,31.573822022193028],[120.27625993387511,31.573873118777456],[120.27602384496866,31.573936439364953],[120.27600371286469,31.57390279512238],[120.27582250845765,31.57358499206426],[120.27574199623312,31.573466419759512],[120.27572186716203,31.573435776355065],[120.27586856802473,31.573423099850135],[120.27657597802384,31.573377202244636],[120.27660611913254,31.57337065150065],[120.27677694815127,31.573361540042004]],[[120.27969266410662,31.574641328299997],[120.27965743815919,31.574589940265763],[120.27955757497818,31.57422160470201],[120.27953137315251,31.574150049661725],[120.27951118288445,31.574060381419777],[120.27948094169176,31.573969892582518],[120.27946482061274,31.57392916755899],[120.27946480136875,31.57391016173027],[120.27946477704943,31.573886154364168],[120.27956122107187,31.573851419676686],[120.2795873311177,31.573831947072943],[120.27953086042926,31.57364389436277],[120.27951068245594,31.57356722983074],[120.27948036871994,31.57340671899365],[120.27917217342261,31.572758022140903],[120.27912787274676,31.572679790556506],[120.27917195334757,31.572545955375304],[120.27917792595196,31.572490829863668],[120.27918797567281,31.57249064963287],[120.27919799103165,31.572457458991806],[120.27922799733695,31.572319875097143],[120.27926418469704,31.57232722919478],[120.27931846053045,31.572333259185335],[120.27978486419914,31.572416956685082],[120.27977088309005,31.572502234318993],[120.27975085953753,31.572575615669415],[120.27973082550666,31.572638993919202],[120.27971683472599,31.572715268763098],[120.27968579240756,31.57282385845094],[120.27966076008666,31.572913334688348],[120.27965077849453,31.57297953471871],[120.27963074604999,31.57304491383438],[120.27962472857324,31.57305702523624],[120.27961072896241,31.573125297681873],[120.27960071008792,31.57315548650196],[120.27959068502801,31.57317967346594],[120.27959069633515,31.57319067689359],[120.27942584775636,31.573161613149363],[120.27946411895982,31.573240954958507],[120.2795306092156,31.573397818240533],[120.27953063480183,31.573422825994633],[120.27953072476444,31.573510853256643],[120.27956099843708,31.573632352231748],[120.27957105530727,31.573639174852094],[120.2796214342311,31.573766316891543],[120.27966172049322,31.57385162575546],[120.27971199454136,31.57387573689839],[120.2798125223362,31.573903954532813],[120.27990307117778,31.57400237450743],[120.28013954017054,31.574296267831993],[120.28024019381013,31.5744505326193],[120.2802050767992,31.574508173518886],[120.27969266410662,31.574641328299997]],[[120.27969266410662,31.574641328299997],[120.27979313658858,31.574614528781964],[120.27980321324445,31.574641357841735],[120.27980322021989,31.574648359952928],[120.27985366971318,31.5748495255927],[120.27989401912204,31.575000855386527],[120.27993435615478,31.575140181764507],[120.27994947012411,31.575179925485106],[120.27995451760883,31.57520284294161],[120.27998478264284,31.575320341777854],[120.28001100397326,31.575413905058582],[120.28004530191026,31.575545336753393],[120.28007554161569,31.575637828502572],[120.28008566320526,31.575711671736574],[120.28007969249377,31.575772796714354],[120.27998524242255,31.575792480841635],[120.27998525887517,31.575809485817178],[120.27991892480202,31.575804663957147],[120.27972399658282,31.575846148469385],[120.2792015048345,31.575953523176175],[120.27907087483305,31.575972872233738],[120.27907074257499,31.57583583223428],[120.27908075455171,31.575796640403084],[120.27907563177052,31.57569570099182],[120.27906046874263,31.575604944880588],[120.27905038689838,31.575572115611838],[120.27904033718407,31.575572296078196],[120.2790402816229,31.575515279271244],[120.27904430541021,31.575519208262993],[120.2790401771008,31.575408247652334],[120.27901996599836,31.57529457484862],[120.27923102679692,31.575307792922406],[120.2794923473364,31.575331123295722],[120.27949634468574,31.57530804460358],[120.2794961766461,31.575136993765113],[120.27949202950803,31.57500802713675],[120.27949190186236,31.5748789885112],[120.27948168429698,31.574710117412547],[120.27969266410662,31.574641328299997]]]";

    @AptAnnotation(desc = "我是 onCreate 上面的注解")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Bird bird = new Bird();
        bird.create();

        Log.i("TAG", "onCreate:------- ");

        findViewById(R.id.btn0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, JetIndexActivity.class));
            }
        });

        findViewById(R.id.btn1).setOnClickListener(view ->
                startActivity(new Intent(HomeActivity.this, ReadSourceActivity.class)));

        findViewById(R.id.btn2).setOnClickListener(view ->
                startActivity(new Intent(HomeActivity.this, CustomViewGuideActivity.class)));

        findViewById(R.id.btn_databinding).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
        });

        findViewById(R.id.btn_Paging).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, MainPagingActivity.class));
        });

        findViewById(R.id.btn_ndk).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, NdkTestActivity.class));
        });
        findViewById(R.id.btn_constraint).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, PracticeGuideActivity.class));
        });
        findViewById(R.id.ll).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, TestMeasureSpecActivity.class));
        });
        findViewById(R.id.windowmanager).setOnClickListener(v -> {
            addOverlay();
        });
        findViewById(R.id.opengl).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, TestActivitySingleTaskActvity.class));
        });

        findViewById(R.id.rxjava).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, com.shixin.ui.rxjava.MainActivity.class));
        });

        findViewById(R.id.thread).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, ThreadActivity.class));
        });

        testGrid();

        //AsyncTask
    }

    private void testGrid() {
        CommonUtils.Point point = new CommonUtils.Point();
        point.x = 120.276355; //longitude
        point.y = 31.573721; //latitude


        Gson gson = new Gson();
        ArrayList<ArrayList<Object>> list
                = gson.fromJson(str, new TypeToken<ArrayList<ArrayList<Object>>>() {
        }.getType());

        for (ArrayList<Object> objects : list) {
            List<CommonUtils.Point> pts = new ArrayList<>();

            for (Object object : objects) {
                ArrayList arrayList = (ArrayList) object;
                double lng = (double) arrayList.get(0);
                double lat = (double) arrayList.get(1);

                CommonUtils.Point point1 = new CommonUtils.Point();
                point1.x = lng; //longitude
                point1.y = lat; //latitude
                pts.add(point1);
            }
            boolean inPolygon = CommonUtils.isInPolygon(point, pts);
            LogUtils.i("是否在范围内=" + inPolygon);

        }
    }

    private boolean askedForOverlayPermission;
    private static final int OVERLAY_PERMISSION_CODE = 0xa1;

    public void addOverlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                askedForOverlayPermission = true;
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_CODE);
            } else {
                Intent serviceIntent = new Intent(HomeActivity.this, WindowManagerDemoActivity.class);
                startActivity(serviceIntent);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_CODE) {
            askedForOverlayPermission = false;
            if (Settings.canDrawOverlays(this)) {
                // SYSTEM_ALERT_WINDOW permission not granted...
                //Toast.makeText(MyProtector.getContext(), "ACTION_MANAGE_OVERLAY_PERMISSION Permission Granted", Toast.LENGTH_SHORT).show();
                Intent serviceIntent = new Intent(HomeActivity.this, WindowManagerDemoActivity.class);
                startService(serviceIntent);
            } else {
                Toast.makeText(getApplicationContext(),
                        "ACTION_MANAGE_OVERLAY_PERMISSION Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
