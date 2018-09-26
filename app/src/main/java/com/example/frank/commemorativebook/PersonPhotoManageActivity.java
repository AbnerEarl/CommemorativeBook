package com.example.frank.commemorativebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.frank.commemorativebook.config.PersonInfo;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonPhotoManageActivity extends AppCompatActivity {

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

    public static  List<String> share_list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_photo);


        all_photo=(Button)this.findViewById(R.id.p_all_photo);
        one_photo=(Button)this.findViewById(R.id.p_one_photo);
        collect_photo=(Button)this.findViewById(R.id.p_collect_photo);
        other_photo=(Button)this.findViewById(R.id.p_other_photo);
        share_photo=(Button)this.findViewById(R.id.btn_share);
        download_photo=(Button)this.findViewById(R.id.btn_download);
        connect_author=(Button)this.findViewById(R.id.btn_communicate);

        share_photo.setText("删除");
        download_photo.setText("编辑");
        connect_author.setText("上传照片");

        stu_id=getIntent().getStringExtra("stu_id");
        gview = (GridView) findViewById(R.id.gv_show_person_photo);
        myAdapter=new MyGridViewAdapter(this);
        //role_list = new ArrayList<Map<String, Object>>();
        dialog = new ProgressDialog(this);
        dialog.setTitle("提示信息");
        dialog.setMessage("正在执行操作，请稍候...");



        //配置适配器
        gview.setAdapter(myAdapter);

        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Map<String, Object> map = myAdapter.list_item.get(i);
                list_show_photo=myAdapter.list_item;
                Intent intent=new Intent(PersonPhotoManageActivity.this,LookPhotoActivity.class);
                startActivity(intent);
            }
        });

        share_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checked_list.size()>0) {
                    String data="";
                    for (Map.Entry<String, Object> entry : checked_list.entrySet()) {
                        String key = entry.getKey().toString();
                        String value = entry.getValue().toString();

                        data=data+myAdapter.list_item.get(Integer.parseInt(key)).get("pic_id").toString()+"#";

                    }
                    deletePhotoById(data);

                }else {
                    Toast.makeText(PersonPhotoManageActivity.this, "请选择需要删除的图片", Toast.LENGTH_SHORT).show();
                }

                //SharePhoto(checked_list);
            }
        });

        download_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checked_list.size()>0) {

                    dialog.show();
                    String data="";
                    for (Map.Entry<String, Object> entry : checked_list.entrySet()) {
                        String key = entry.getKey().toString();
                        String value = entry.getValue().toString();
                        // System.out.println("key=" + key + " value=" + value);
                        data=data+myAdapter.list_item.get(Integer.parseInt(key)).get("pic_id").toString()+"#";

                    }


                    LinearLayout linearLayout=new LinearLayout(PersonPhotoManageActivity.this);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    Button photo_collect=new Button(PersonPhotoManageActivity.this);
                    photo_collect.setText("集体");
                    Button photo_one=new Button(PersonPhotoManageActivity.this);
                    photo_one.setText("个人");
                    Button photo_other=new Button(PersonPhotoManageActivity.this);
                    photo_other.setText("其他");

                    linearLayout.addView(photo_collect);
                    linearLayout.addView(photo_one);
                    linearLayout.addView(photo_other);


                    final AlertDialog photo_dialog=new  AlertDialog.Builder(PersonPhotoManageActivity.this).create();
                    photo_dialog.setView(linearLayout);
                    photo_dialog.setTitle("请选择需要移动到的目标相册：");
                    photo_dialog.show();
                    final String finalData = data;
                    photo_collect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            photo_dialog.dismiss();
                            String dd="2#"+ finalData;
                            removePhotoById(dd);
                        }
                    });


                    photo_one.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            photo_dialog.dismiss();
                            String dd="1#"+ finalData;
                            removePhotoById(dd);
                        }
                    });

                    photo_other.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            photo_dialog.dismiss();
                            String dd="3#"+ finalData;
                            removePhotoById(dd);
                        }
                    });

                }else {
                    Toast.makeText(PersonPhotoManageActivity.this, "请选择需要编辑的图片", Toast.LENGTH_SHORT).show();
                }

            }
        });

        connect_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PersonPhotoManageActivity.this,ConnectAuthorActivity.class);
                startActivity(intent);
            }
        });

        all_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhotoById(PersonInfo.userAccount+"#10#");
            }
        });
        one_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhotoById(PersonInfo.userAccount+"#1#");
            }
        });
        collect_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhotoById(PersonInfo.userAccount+"#2#");
            }
        });
        other_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhotoById(PersonInfo.userAccount+"#3#");
            }
        });

        getPhotoById(PersonInfo.userAccount+"#10");
    }


    public void SharePhoto(String photoUri,final Activity activity) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        File file = new File(photoUri);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, activity.getTitle()));
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
            dialog.dismiss();
        }
    }


    private void saveBitmap(Bitmap bitmap,String image_Path) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "同学录图片文件夹");
        if (!appDir.exists()) appDir.mkdir();
        String[] str = image_Path.split("/");
        String fileName = str[str.length - 1];
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            //file.getAbsolutePath();//获取保存的图片的文件名
            //    onSaveSuccess(file);
        } catch (IOException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(PersonPhotoManageActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                }
            });
            e.printStackTrace();
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

            Glide.with(PersonPhotoManageActivity.this).load(list_item.get(position).get("image").toString()).into(viewHolder.imageview_thumbnail);
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
                    ApiService.GetString(PersonPhotoManageActivity.this, "getPhotoByStuId", paremetes, new RxStringCallback() {
                        @Override
                        public void onNext(Object tag, String response) {
                            myAdapter.list_item.clear();
                            String result=response.toString().trim();
                            if (result.contains("#")){

                                String pic_data[]=result.split("##");
                                for (int i=0;i+1<pic_data.length;i=i+2){

                                    Map<String, Object> map1 =new HashMap<>();
                                    map1.put("image",pic_data[i+1]);
                                    map1.put("pic_id",pic_data[i]);
                                    myAdapter.list_item.add(map1);
                                }


                            }
                            myAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(PersonPhotoManageActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(PersonPhotoManageActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }



    private void deletePhotoById(final String data){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data",data);
                    ApiService.GetString(PersonPhotoManageActivity.this, "deletePhotoByStuId", paremetes, new RxStringCallback() {
                        @Override
                        public void onNext(Object tag, String response) {
                            if (response.trim().equals("true")){
                                Toast.makeText(PersonPhotoManageActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                                finish();

                            }else {
                                Toast.makeText(PersonPhotoManageActivity.this, "操作失败" , Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(PersonPhotoManageActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(PersonPhotoManageActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }




    private void removePhotoById(final String data){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data",data);
                    ApiService.GetString(PersonPhotoManageActivity.this, "removePhotoByStuId", paremetes, new RxStringCallback() {
                        @Override
                        public void onNext(Object tag, String response) {
                            dialog.dismiss();
                            if (response.trim().equals("true")){
                                Toast.makeText(PersonPhotoManageActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                                finish();

                            }else {
                                Toast.makeText(PersonPhotoManageActivity.this, "操作失败" , Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(PersonPhotoManageActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                            dialog.dismiss();
                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(PersonPhotoManageActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                            dialog.dismiss();
                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }



}
