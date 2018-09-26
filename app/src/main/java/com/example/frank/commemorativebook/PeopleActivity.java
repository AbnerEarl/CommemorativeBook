package com.example.frank.commemorativebook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeopleActivity extends AppCompatActivity {

//    private List<Map<String, Object>> role_list;
    private GridView gview;
    private SimpleAdapter sim_adapter;
    private MyGridViewAdapter myAdapter;
    private String class_id="",keyword="",tag="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        tag=getIntent().getStringExtra("tag");
        if (tag.equals("0")){
            class_id=getIntent().getStringExtra("class_id");
        }else {
            keyword=getIntent().getStringExtra("keyword");
        }


        System.out.println("班级id："+class_id);

        gview = (GridView) findViewById(R.id.gv_people_info);
        myAdapter=new MyGridViewAdapter(this);
        //role_list = new ArrayList<Map<String, Object>>();

        /*Map<String, Object> map =new HashMap<>();
        map.put("image","http://cdn.v2ex.co/gravatar/becb0d5c59469a34a54156caef738e90?s=73&d=retro");
        map.put("text","张三");
        myAdapter.list_item.add(map);

        Map<String, Object> map1 =new HashMap<>();
        map1.put("image","http://cdn.v2ex.co/gravatar/becb0d5c59469a34a54156caef738e90?s=73&d=retro");
        map1.put("text","李四");
        myAdapter.list_item.add(map1);*/

        //新建适配器
       // String [] from ={"image","text"};
        //int [] to = {R.id.login_role_image,R.id.login_role_name};
        //sim_adapter = new SimpleAdapter(this, role_list, R.layout.item_role_login, from, to);


        //配置适配器
        gview.setAdapter(myAdapter);

        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map = myAdapter.list_item.get(i);
                Intent intent=new Intent(PeopleActivity.this,PersonPhotoActivity.class);
                intent.putExtra("stu_id",map.get("stu_id").toString());
                startActivity(intent);
            }
        });


        if (tag.equals("0")){
            getAllPeopleByClass(class_id);
        }else {

            getAllPeopleByName(keyword);
        }

    }





    public class MyGridViewAdapter extends BaseAdapter {

        private Context mContext = null;
        private LayoutInflater mLayoutInflater = null;
//        private List<String> mList = null;
         private List<Map<String, Object>> list_item= new ArrayList<Map<String, Object>>();;

        //private int width = 120;//每个Item的宽度,可以根据实际情况修改
        //private int height = 150;//每个Item的高度,可以根据实际情况修改


        public  class MyGridViewHolder{
            public ImageView imageview_thumbnail;
            public TextView textview_test;
        }

        public MyGridViewAdapter(Context context) {
            // TODO Auto-generated constructor stub
            this.mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
        }


        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list_item.size();
        }


        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }


        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            MyGridViewHolder viewHolder = null;
            if(convertView == null){
                viewHolder = new MyGridViewHolder();
                convertView = mLayoutInflater.inflate(R.layout.item_role_login, null);
                viewHolder.imageview_thumbnail = (ImageView)convertView.findViewById(R.id.login_role_image);
                viewHolder.textview_test = (TextView)convertView.findViewById(R.id.login_role_name);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (MyGridViewHolder)convertView.getTag();
            }

            Glide.with(PeopleActivity.this).load(list_item.get(position).get("image").toString()).into(viewHolder.imageview_thumbnail);
            viewHolder.textview_test.setText(list_item.get(position).get("text").toString());






            return convertView;
        }



    }










    private void getAllPeopleByClass(final String class_id){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data",class_id);
                    ApiService.GetString(PeopleActivity.this, "getAllPeopleByClass", paremetes, new RxStringCallback() {
                        @Override
                        public void onNext(Object tag, String response) {

                            String result=response.toString().trim();
                            if (result.contains("#")){
                                myAdapter.list_item.clear();
                                String class_data[]=result.split("##");
                                for (int i=0;i<class_data.length;i++){
                                    String person_info[]=class_data[i].split("#");
                                    Map<String, Object> map1 =new HashMap<>();
                                    map1.put("stu_id",person_info[0]);
                                    map1.put("text",person_info[1]);
                                    map1.put("image",person_info[2]);
                                    myAdapter.list_item.add(map1);
                                }

                                myAdapter.notifyDataSetChanged();
                            }

                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(PeopleActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(PeopleActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }





    private void getAllPeopleByName(final String keyword){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data",keyword);
                    ApiService.GetString(PeopleActivity.this, "getStuByName", paremetes, new RxStringCallback() {
                        @Override
                        public void onNext(Object tag, String response) {

                            String result=response.toString().trim();
                            if (result.contains("#")){
                                myAdapter.list_item.clear();
                                String class_data[]=result.split("##");
                                for (int i=0;i<class_data.length;i++){
                                    String person_info[]=class_data[i].split("#");
                                    Map<String, Object> map1 =new HashMap<>();
                                    map1.put("stu_id",person_info[0]);
                                    map1.put("text",person_info[1]);
                                    map1.put("image",person_info[2]);
                                    myAdapter.list_item.add(map1);
                                }

                                myAdapter.notifyDataSetChanged();
                            }

                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(PeopleActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(PeopleActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }



}
