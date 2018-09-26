package com.example.frank.commemorativebook;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.frank.commemorativebook.config.PersonInfo;
import com.example.frank.commemorativebook.config.URLConfig;
import com.example.frank.commemorativebook.utils.FtpClientUpload;
import com.example.frank.commemorativebook.utils.SaveImage;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ConnectAuthorActivity extends AppCompatActivity {

    private Button btn_upload,btn_select_photo;
    private List<String> list = new ArrayList<String>();
    private Spinner opionsp;
    private ArrayAdapter<String> spadapter;
    private MyGridViewAdapter myAdapter;
    private GridView gview;
    private ProgressDialog dialog_proceess;
    private int tag=-1;
    private int upload_flag=0;
    public static ArrayList<String> mDataList = new ArrayList<>();//存储选取图片路径
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_author);

        btn_upload=(Button)this.findViewById(R.id.btn_upload);
        btn_select_photo=(Button)this.findViewById(R.id.btn_select_photo);
        opionsp=(Spinner)this.findViewById(R.id.sp_pic_style);
        gview = (GridView) findViewById(R.id.gv_wait_upload);
        myAdapter=new MyGridViewAdapter(this);
        gview.setAdapter(myAdapter);
        list.add("请选择检相册：");
        list.add("集体");
        list.add("个人");
        list.add("其他");

        dialog_proceess = new ProgressDialog(this);
        dialog_proceess.setTitle("提示信息");
        dialog_proceess.setMessage("正在上传，请稍候...");


        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        spadapter  =  new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,  list);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        spadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        opionsp.setAdapter(spadapter);

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tag!=-1&&myAdapter.list_item.size()>0) {

                    final EditText et_description = new EditText(ConnectAuthorActivity.this);
                    et_description.setHint("请输入照片的相关描述！");

                    new AlertDialog.Builder(ConnectAuthorActivity.this)
                            .setTitle("照片描述")
                            .setView(et_description)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog_proceess.show();

                                            final String description = et_description.getText().toString();
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        //文件测试
                                                        String folderName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                                                        for (int i = 0; i < myAdapter.list_item.size(); i++) {


                                                            //上传文件到服务器
                                                            String eqfilename = myAdapter.list_item.get(i).get("image").toString();
                                                            String datafilename = eqfilename.substring(eqfilename.lastIndexOf("/") + 1, eqfilename.length());
                                                            FtpClientUpload.UploadFile(eqfilename, PersonInfo.userAccount + "/" + folderName + "/", ConnectAuthorActivity.this, datafilename);

                                                            //上传文字描述到服务器
                                                            String dd = PersonInfo.userAccount + "##" + datafilename + "##" + URLConfig.CompanyURL + folderName + "/" + datafilename + "##" + tag + "##" + description + "##";
                                                            Map<String, Object> paremetes = new HashMap<>();
                                                            paremetes.put("data", dd);
                                                            ApiService.GetString(ConnectAuthorActivity.this, "addPhotoByStuId", paremetes, new RxStringCallback() {
                                                                boolean flag = false;

                                                                @Override
                                                                public void onNext(Object tag, String response) {

                                                                    if (response.trim().equals("true")) {
                                                                        upload_flag++;

                                                                    }
                                                                    if (upload_flag >= myAdapter.list_item.size()) {

                                                                        dialog_proceess.dismiss();
                                                                        Toast.makeText(ConnectAuthorActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                                                                        finish();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onError(Object tag, Throwable e) {
                                                                    dialog_proceess.dismiss();
                                                                    Toast.makeText(ConnectAuthorActivity.this, "网络发生错误" + e, Toast.LENGTH_SHORT).show();
                                                                }

                                                                @Override
                                                                public void onCancel(Object tag, Throwable e) {
                                                                    dialog_proceess.dismiss();
                                                                    Toast.makeText(ConnectAuthorActivity.this, "网络发生错误" + e, Toast.LENGTH_SHORT).show();

                                                                }


                                                            });

                                                        }


                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }).start();

                                        }
                                    }).show();
                }else if (tag==-1){
                    Toast.makeText(ConnectAuthorActivity.this, "请选择需要上传的目标相册", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(ConnectAuthorActivity.this, "请选择需要上传的照片", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btn_select_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectAuthorActivity.this, MyPhotoActivity.class);
                //startActivity(intent);
                startActivityForResult(intent, 7777);

            }
        });


        opionsp.setOnItemSelectedListener(new  Spinner.OnItemSelectedListener(){
            public  void  onItemSelected(AdapterView<?> arg0, View  arg1, int  arg2, long  arg3)  {
                //  TODO  Auto-generated  method  stub

                if (opionsp.getSelectedItem().equals("集体")){

                    tag=2;

                }else if (opionsp.getSelectedItem().equals("个人")){

                    tag=1;
                }else if (opionsp.getSelectedItem().equals("其他")){

                    tag=3;
                }



            }
            public  void  onNothingSelected(AdapterView<?>  arg0)  {
                //  TODO  Auto-generated  method  stub
                //myTextView.setText("NONE");
            }
        });


        //*下拉菜单弹出的内容选项触屏事件处理*//*
        opionsp.setOnTouchListener(new  Spinner.OnTouchListener(){
            public  boolean  onTouch(View  v,  MotionEvent event)  {
                //  TODO  Auto-generated  method  stub

                return  false;
            }
        });
        opionsp.setOnFocusChangeListener(new  Spinner.OnFocusChangeListener(){
            public  void  onFocusChange(View  v,  boolean  hasFocus)  {
                //  TODO  Auto-generated  method  stub

            }
        });




    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        String path = Environment.getExternalStorageDirectory() + "/同学录图片文件夹/upload_images/"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"/";

        if (requestCode ==7777) {
            Random random=new Random();
            for (int i=0;i<mDataList.size();i++){
                System.out.println("返回的路径："+mDataList.get(i));
                //设置自定义照片的名字

                String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+random.nextInt(1000);
                String tem_filename=path  + fileName + ".jpg";
                SaveImage.saveBitmap(mDataList.get(i),fileName);

                HashMap<String, Object> map=new HashMap<>();
                map.put("image", tem_filename);
                //map.put("ItemText", "请填写照片相关描述");
                myAdapter.list_item.add(map);

            }
            myAdapter.notifyDataSetChanged();

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
                convertView = mLayoutInflater.inflate(R.layout.item_upload_photo, null);
                viewHolder.imageview_thumbnail = (ImageView)convertView.findViewById(R.id.img_upload_photo);

                convertView.setTag(viewHolder);
            }else{
                viewHolder = (MyGridViewAdapter.MyGridViewHolder)convertView.getTag();
            }

            //Glide.with(ConnectAuthorActivity.this).load(list_item.get(position).get("image").toString()).into(viewHolder.imageview_thumbnail);
            //viewHolder.checkBox_select.setText(list_item.get(position).get("text").toString());
            Bitmap bm = BitmapFactory.decodeFile(list_item.get(position).get("image").toString());
            viewHolder.imageview_thumbnail.setImageBitmap(bm);



            return convertView;
        }



    }





}
