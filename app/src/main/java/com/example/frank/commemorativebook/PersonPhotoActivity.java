package com.example.frank.commemorativebook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.frank.commemorativebook.social.share.*;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class PersonPhotoActivity extends AppCompatActivity {

    //private List<Map<String, Object>> role_list;
    private GridView gview;
    //private SimpleAdapter sim_adapter;
    //private String image_Path = "http://e.hiphotos.baidu.com/image/pic/item/2fdda3cc7cd98d10b510fdea233fb80e7aec9021.jpg";
    private Button all_photo,one_photo,collect_photo,other_photo,share_photo,download_photo,connect_author;
    private ProgressDialog dialog;
    private MyGridViewAdapter myAdapter;
    private String stu_id="";
    public static List<Map<String, Object>> list_show_photo= new ArrayList<Map<String, Object>>();
    private Map<String, Object> checked_list= new HashMap<String, Object>();
    private static boolean share_flag=false;
    private String share_img_path="";

    public static  List<String> share_list = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_photo);

        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();


        all_photo=(Button)this.findViewById(R.id.p_all_photo);
        one_photo=(Button)this.findViewById(R.id.p_one_photo);
        collect_photo=(Button)this.findViewById(R.id.p_collect_photo);
        other_photo=(Button)this.findViewById(R.id.p_other_photo);
        share_photo=(Button)this.findViewById(R.id.btn_share);
        download_photo=(Button)this.findViewById(R.id.btn_download);
        connect_author=(Button)this.findViewById(R.id.btn_communicate);

        stu_id=getIntent().getStringExtra("stu_id");
        gview = (GridView) findViewById(R.id.gv_show_person_photo);
        myAdapter=new MyGridViewAdapter(this);
        //role_list = new ArrayList<Map<String, Object>>();
        dialog = new ProgressDialog(this);
        dialog.setTitle("提示信息");
        dialog.setMessage("正在下载，请稍候...");


       /* Map<String, Object> map =new HashMap<>();
        map.put("image",R.drawable.report);
        //map.put("text","");
        role_list.add(map);

        Map<String, Object> map1 =new HashMap<>();
        map1.put("image",R.drawable.report);
       // map1.put("text","");
        role_list.add(map1);*/

        //新建适配器
        /*String [] from ={"image","text"};
        int [] to = {R.id.img_show_photo,R.id.check_box_select};
        sim_adapter = new SimpleAdapter(this, role_list, R.layout.item_show_photo, from, to);*/


        //配置适配器
        gview.setAdapter(myAdapter);

        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Map<String, Object> map = myAdapter.list_item.get(i);
                list_show_photo=myAdapter.list_item;
                Intent intent=new Intent(PersonPhotoActivity.this,LookPhotoActivity.class);
                startActivity(intent);
            }
        });

        share_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checked_list.size()>0) {
                    List<String> list = new ArrayList<>();
                    for (Map.Entry<String, Object> entry : checked_list.entrySet()) {
                        String key = entry.getKey().toString();
                        String value = entry.getValue().toString();
                        list.add(value);

                    }
                    share_list=list;
                    /*Intent intent = new Intent(PersonPhotoActivity.this, com.example.frank.commemorativebook.social.share.MainActivity.class);
                    intent.putExtra("url", list.get(0));
                    startActivity(intent);*/
                    share_flag=true;
                    new MYTask().execute( list.get(0));


                }else {
                    Toast.makeText(PersonPhotoActivity.this, "请选择需要分享的图片", Toast.LENGTH_SHORT).show();
                }

                //SharePhoto(checked_list);
            }
        });

        download_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checked_list.size()>0) {
                    for (Map.Entry<String, Object> entry : checked_list.entrySet()) {
                        String key = entry.getKey().toString();
                        String value = entry.getValue().toString();
                        System.out.println("key=" + key + " value=" + value);
                        new MYTask().execute(value);
                    }
                }else {
                    Toast.makeText(PersonPhotoActivity.this, "请选择需要下载的图片", Toast.LENGTH_SHORT).show();
                }

            }
        });

        connect_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PersonPhotoActivity.this,AuthorInfoActivity.class);
                startActivity(intent);
            }
        });

        all_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhotoById(stu_id+"#10");
            }
        });
        one_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhotoById(stu_id+"#1");
            }
        });
        collect_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhotoById(stu_id+"#2");
            }
        });
        other_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhotoById(stu_id+"#3");
            }
        });

        getPhotoById(stu_id+"#10");
    }


    public void SharePhoto(String photoUri,final Activity activity) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        File file = new File(photoUri);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, activity.getTitle()));
    }

    public static void sharedToQQ(Context context,Uri uri,String filePath){
       // if (sendMsg == null) return;
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        //intent.setType("text/plain");  //文本分享
        intent.setType("image/*");
        if (uri != null) {
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        } else {
            intent.putExtra(Intent.EXTRA_STREAM, filePath);
        }
        intent.setPackage("com.tencent.mobileqq");
        intent.setClassName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");//QQ
        context.startActivity(intent);
    }

    public class MYTask extends AsyncTask<String, Void, Bitmap> {
        /**
         * 表示任务执行之前的操作
         */
        String url="";
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog.show();
        }

        /**
         * 主要是完成耗时的操作
         */
        @Override
        protected Bitmap doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            // 使用网络连接类HttpClient类王城对网络数据的提取
            url=arg0[0];
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(arg0[0]);
            Bitmap bitmap = null;
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    byte[] data = EntityUtils.toByteArray(httpEntity);
                    bitmap = BitmapFactory
                            .decodeByteArray(data, 0, data.length);
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //imageView.setImageBitmap(result);
            /*Random random=new Random();
            String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+random.nextInt(1000);*/
            saveBitmap(result,url);
//            dialog.dismiss();
        }
    }


    private void saveBitmap(Bitmap bitmap,String image_Path) {
       /* File appDir = new File(Environment.getExternalStorageDirectory(), "同学录图片文件夹");
        if (!appDir.exists()) appDir.mkdir();
        String[] str = image_Path.split("/");
        String fileName = str[str.length - 1];
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            share_img_path=appDir+"/"+fileName;
            //file.getAbsolutePath();//获取保存的图片的文件名
            //    onSaveSuccess(file);
        } catch (IOException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(PersonPhotoActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                }
            });
            e.printStackTrace();
        }
        dialog.dismiss();
        if (share_flag){
            SharePhoto(share_img_path,PersonPhotoActivity.this);
            share_flag=false;
        }*/

        File appDir = new File(Environment.getExternalStorageDirectory(), "MyImages");
        if (!appDir.exists()) appDir.mkdir();
        /*String[] str = image_Path.split("/");
        String fileName = str[str.length - 1];*/
        String fileName=UUID.randomUUID().toString()+".png";
        File file = new File(appDir, fileName);
        Log.i("图片信息", "saveBitmap: 开始操作文件！");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            share_img_path=appDir+"/"+fileName;
            //file.getAbsolutePath();//获取保存的图片的文件名
            //    onSaveSuccess(file);
        } catch (IOException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(PersonPhotoActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                }
            });
            e.printStackTrace();
        }
        dialog.dismiss();
        if (share_flag){
            share_flag=false;
            SharePhoto(share_img_path,PersonPhotoActivity.this);
        }

    }


    public class MyGridViewAdapter extends BaseAdapter {

        private Context mContext = null;
        private LayoutInflater mLayoutInflater = null;
        //        private List<String> mList = null;
        private List<Map<String, Object>> list_item= new ArrayList<Map<String, Object>>();


        //private int width = 120;//每个Item的宽度,可以根据实际情况修改
        //private int height = 150;//每个Item的高度,可以根据实际情况修改


        public  class MyGridViewHolder{
            public ImageView imageview_thumbnail;
            public CheckBox checkBox_select;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            MyGridViewAdapter.MyGridViewHolder viewHolder = null;
            if(convertView == null){
                viewHolder = new MyGridViewAdapter.MyGridViewHolder();
                convertView = mLayoutInflater.inflate(R.layout.item_show_photo, null);
                viewHolder.imageview_thumbnail = (ImageView)convertView.findViewById(R.id.img_show_photo);
                viewHolder.checkBox_select = (CheckBox) convertView.findViewById(R.id.check_box_select);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (MyGridViewAdapter.MyGridViewHolder)convertView.getTag();
            }

            Glide.with(PersonPhotoActivity.this).load(list_item.get(position).get("image").toString()).into(viewHolder.imageview_thumbnail);
            //viewHolder.checkBox_select.setText(list_item.get(position).get("text").toString());


            viewHolder.checkBox_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        //选中操作
                        Map<String ,Object> map=list_item.get(position);
                        checked_list.put(position+"",map.get("image"));
                    }else {
                        checked_list.remove(position+"");
                    }
                }
            });




            return convertView;
        }



    }


    private void getPhotoById(final String data){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data",data);
                    ApiService.GetString(PersonPhotoActivity.this, "getPhotoById", paremetes, new RxStringCallback() {
                        @Override
                        public void onNext(Object tag, String response) {
                            myAdapter.list_item.clear();
                            String result=response.toString().trim();
                            if (result.contains("#")){

                                String pic_data[]=result.split("##");
                                for (int i=0;i<pic_data.length;i++){

                                    Map<String, Object> map1 =new HashMap<>();
                                    map1.put("image",pic_data[i]);
                                    myAdapter.list_item.add(map1);
                                }


                            }
                            myAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(PersonPhotoActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(PersonPhotoActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }



}
