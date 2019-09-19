package com.sky.chowder.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sky.chowder.R;
import com.sky.chowder.model.Employe;
import com.sky.chowder.ui.adapter.HrmAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by libin on 2018/9/6 上午10:41.
 */
public class TestActivity extends AppCompatActivity {
    String json = "[\n" +
            "            {\n" +
            "                \"userId\": \"975577\",\n" +
            "                \"name\": \"Test_115\",\n" +
            "                \"dept\": \"部门1\",\n" +
            "                \"mobile\": \"11500000000\",\n" +
            "                \"date\": \"1567699200000\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"userId\": \"975612\",\n" +
            "                \"name\": \"Test_1902\",\n" +
            "                \"sn\": \"HZ00002\",\n" +
            "                \"dept\": \"部门1\",\n" +
            "                \"mobile\": \"11900000002\",\n" +
            "                \"date\": \"1567958400000\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"userId\": \"975614\",\n" +
            "                \"name\": \"Test_1903\",\n" +
            "                \"sn\": \"HZ00003\",\n" +
            "                \"dept\": \"销售部/销售一组,乖宝宝\",\n" +
            "                \"mobile\": \"11900000003\",\n" +
            "                \"date\": \"1567958400000\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"userId\": \"975616\",\n" +
            "                \"name\": \"Test_1904\",\n" +
            "                \"sn\": \"\",\n" +
            "                \"dept\": \"霖珑_Test119\",\n" +
            "                \"mobile\": \"11900000004\",\n" +
            "                \"date\": \"1568044800000\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"userId\": \"975618\",\n" +
            "                \"name\": \"Test_1905\",\n" +
            "                \"sn\": \"\",\n" +
            "                \"dept\": \"财务中心/财务部\",\n" +
            "                \"mobile\": \"11900000005\",\n" +
            "                \"date\": \"1568044800000\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"userId\": \"975620\",\n" +
            "                \"name\": \"Test_1906\",\n" +
            "                \"sn\": \"0077\",\n" +
            "                \"dept\": \"霖珑_Test119\",\n" +
            "                \"mobile\": \"11900000006\",\n" +
            "                \"date\": \"1568131200000\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"userId\": \"975622\",\n" +
            "                \"name\": \"Test_1907\",\n" +
            "                \"sn\": \"\",\n" +
            "                \"dept\": \"霖珑_Test119\",\n" +
            "                \"mobile\": \"11900000007\",\n" +
            "                \"date\": \"1568217600000\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"userId\": \"975624\",\n" +
            "                \"name\": \"Test_1908\",\n" +
            "                \"sn\": \"\",\n" +
            "                \"dept\": \"霖珑_Test119\",\n" +
            "                \"mobile\": \"11900000008\",\n" +
            "                \"date\": \"1568304000000\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"userId\": \"975626\",\n" +
            "                \"name\": \"Test_1909\",\n" +
            "                \"sn\": \"\",\n" +
            "                \"dept\": \"霖珑_Test119\",\n" +
            "                \"mobile\": \"11900000009\",\n" +
            "                \"date\": \"1568563200000\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"userId\": \"975632\",\n" +
            "                \"name\": \"Test_1912\",\n" +
            "                \"sn\": \"\",\n" +
            "                \"dept\": \"行政管理中心/人事部\",\n" +
            "                \"mobile\": \"11900000012\",\n" +
            "                \"date\": \"1567785600000\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"userId\": \"975640\",\n" +
            "                \"name\": \"Test_1916\",\n" +
            "                \"sn\": \"\",\n" +
            "                \"dept\": \"霖珑_Test119\",\n" +
            "                \"mobile\": \"11900000016\",\n" +
            "                \"date\": \"1567785600000\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"userId\": \"99669600092\",\n" +
            "                \"name\": \"Test_1919\",\n" +
            "                \"sn\": \"\",\n" +
            "                \"dept\": \"霖珑_Test119\",\n" +
            "                \"mobile\": \"11900000019\",\n" +
            "                \"date\": \"1567699200000\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"userId\": \"975967\",\n" +
            "                \"name\": \"zi\",\n" +
            "                \"dept\": \"霖珑_Test119\",\n" +
            "                \"mobile\": \"18601127146\",\n" +
            "                \"date\": \"1567785600000\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"userId\": \"99669600097\",\n" +
            "                \"name\": \"Test_1923\",\n" +
            "                \"sn\": \"\",\n" +
            "                \"dept\": \"霖珑_Test119\",\n" +
            "                \"mobile\": \"11900000023\",\n" +
            "                \"date\": \"1568044800000\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"userId\": \"99669600099\",\n" +
            "                \"name\": \"Test_1924\",\n" +
            "                \"sn\": \"\",\n" +
            "                \"dept\": \"财务中心/财务部\",\n" +
            "                \"mobile\": \"11900000024\",\n" +
            "                \"date\": \"1568044800000\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"userId\": \"99669600101\",\n" +
            "                \"name\": \"Test_1925\",\n" +
            "                \"sn\": \"\",\n" +
            "                \"dept\": \"霖珑_Test119\",\n" +
            "                \"mobile\": \"11900000025\",\n" +
            "                \"date\": \"1568131200000\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"userId\": \"99669600103\",\n" +
            "                \"name\": \"Test_1926\",\n" +
            "                \"sn\": \"\",\n" +
            "                \"dept\": \"霖珑_Test119\",\n" +
            "                \"mobile\": \"11900000026\",\n" +
            "                \"date\": \"1568217600000\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"userId\": \"99669600105\",\n" +
            "                \"name\": \"Test_1927\",\n" +
            "                \"sn\": \"\",\n" +
            "                \"dept\": \"霖珑_Test119\",\n" +
            "                \"mobile\": \"11900000027\",\n" +
            "                \"date\": \"1568304000000\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"userId\": \"99669600107\",\n" +
            "                \"name\": \"Test_1928\",\n" +
            "                \"sn\": \"\",\n" +
            "                \"dept\": \"霖珑_Test119\",\n" +
            "                \"mobile\": \"11900000028\",\n" +
            "                \"date\": \"1568563200000\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"userId\": \"99669600113\",\n" +
            "                \"name\": \"Test_1931\",\n" +
            "                \"sn\": \"\",\n" +
            "                \"dept\": \"行政管理中心/人事部\",\n" +
            "                \"mobile\": \"11900000031\",\n" +
            "                \"date\": \"1567785600000\"\n" +
            "            }\n" +
            "        ]";
    //    @BindView(R.id.linear_biaoge)
//    LinearLayout linearBiaoge;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        Gson gson = new Gson();
        List<Employe> employes = gson.fromJson(json, new TypeToken<List<Employe>>() {
        }.getType());
        Employe employe = new Employe("姓名", "工号", "部门", "登录号码", "入职日期");
        employes.add(0, employe);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        HrmAdapter adapter = new HrmAdapter(employes);
        recycler.setAdapter(adapter);
//        setEmployees(employes);
    }

    private void setEmployees(List<Employe> employees) {
        if (employees.isEmpty()) return;
        View view;
        LinearLayout linear;
        TextView tv01;
        TextView tv02;
        TextView tv03;
        TextView tv04;
        TextView tv05;
        for (int i = -1; i < employees.size(); i++) {
            view = LayoutInflater.from(this).inflate(R.layout.adapter_hrm_five, null);
            linear = view.findViewById(R.id.linear);
            tv01 = view.findViewById(R.id.tv01);
            tv02 = view.findViewById(R.id.tv02);
            tv03 = view.findViewById(R.id.tv03);
            tv04 = view.findViewById(R.id.tv04);
            tv05 = view.findViewById(R.id.tv05);
            if (i == -1) {
                linear.setShowDividers(0);
                linear.setBackgroundColor(getResources().getColor(R.color.color_F0F0F0));
                tv01.setText("姓名");
                tv02.setText("工号");
                tv03.setText("部门");
                tv04.setText("登录号码");
                tv05.setText("入职日期");
            } else {
                tv01.setText(employees.get(i).getName());
                tv02.setText(employees.get(i).getSn());
                tv03.setText(employees.get(i).getDept());
                tv04.setText(employees.get(i).getMobile());
                tv05.setText(employees.get(i).getDate());
            }
//            linearBiaoge.addView(view);
        }
    }

}