package com.example.frank.commemorativebook;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private TextView t_time;
    private ImageView img_welcome;

    private  int tag=3;
    private  boolean browse=false;
    private String addUrl="https://jingyan.baidu.com/article/19020a0af8982d529d2842ba.html";
    private String wlecomUrl="http://114.116.94.16:8080/SpecialDeviceExam/static/Image/Advertise/welcome.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_start);

        t_time=(TextView)this.findViewById(R.id.text_time);
        img_welcome=(ImageView)this.findViewById(R.id.img_welcome);
        String keysig=UUID.randomUUID().toString();
        Glide.with(MainActivity.this).load(wlecomUrl).signature(new StringSignature(keysig)).into(img_welcome);

        getAddUrl();

        img_welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ApplicationLoad.class);
                intent.putExtra("url",addUrl);
                //startActivity(intent);
                startActivityForResult(intent,666);
                browse=true;

            }
        });



        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                handler.postDelayed(this, 1000);
                if (!browse) {


                    t_time.setText("倒计时 " + tag + "（s）");

                    if (tag <= 0) {
                        browse=true;
                        //Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        //intent.putExtra("tag","100");
                        startActivity(intent);
                        finish();
                    }
                    tag--;
                }

            }
        };
        handler.postDelayed(runnable, 1000);


       /* new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    *//*for (int i=0;i<3;i++){
                        t_time.setText("倒计时"+(3-i)+"（s）");
                        Thread.sleep(1000);
                    }*//*

                    t_time.setText("倒计时"+(3)+"（s）");
                    Thread.sleep(1000);
                    t_time.setText("倒计时"+(2)+"（s）");
                    Thread.sleep(1000);
                    t_time.setText("倒计时"+(1)+"（s）");
                    Thread.sleep(1000);



                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                intent.putExtra("tag","100");
                startActivity(intent);
                finish();
            }
        }).start();*/
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==666){
            browse=false;
        }
    }



    private void getAddUrl(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data","getallclass");
                    ApiService.GetString(MainActivity.this, "getAddUrl", paremetes, new RxStringCallback() {
                        @Override
                        public void onNext(Object tag, String response) {

                            String result=response.toString().trim();
                            if(!result.equals("")&&result.length()>5){
                                addUrl=result;
                            }

                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            //Toast.makeText(HomeActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                           // Toast.makeText(HomeActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


    private void getPhotoById(final String data){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data",data);
                    ApiService.GetString(MainActivity.this, "getPhotoById", paremetes, new RxStringCallback() {
                        @Override
                        public void onNext(Object tag, String response) {
                            //myAdapter.list_item.clear();
                            String result=response.toString().trim();
                            if (result.contains("#")){

                                String pic_data[]=result.split("##");
                                for (int i=0;i<pic_data.length;i++){

                                    Map<String, Object> map1 =new HashMap<>();
                                    map1.put("image",pic_data[i]);
                                   // myAdapter.list_item.add(map1);
                                }


                            }
                            //myAdapter.notifyDataSetChanged();
                            //Glide.with(MainActivity.this).load(list_item.get(position).get("image").toString()).into(viewHolder.imageview_thumbnail);
                            //viewHolder.checkBox_select.setText(list_item.get(position).get("text").toString());
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(MainActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(MainActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


}
