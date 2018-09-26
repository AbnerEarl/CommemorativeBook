package com.example.frank.commemorativebook;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {


    private PermissMyAdapter permissMyAdapter;
    private ListView lv_message;
    private Button btn_search;
    private EditText et_search_name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //绑定控件
        btn_search=(Button)this.findViewById(R.id.btn_search);
        et_search_name=(EditText)this.findViewById(R.id.et_search_name);
        lv_message = (ListView) this.findViewById(R.id.lv_classes);
        permissMyAdapter=new PermissMyAdapter(this);
        lv_message.setAdapter(permissMyAdapter);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword=et_search_name.getText().toString().trim();
                if (keyword.length()<1){
                    Toast.makeText(HomeActivity.this,"请输入需要查找的姓名",Toast.LENGTH_LONG).show();
                }else {
                    Intent intent=new Intent(HomeActivity.this,PeopleActivity.class);
                    intent.putExtra("keyword",keyword);
                    intent.putExtra("tag","1");
                    startActivity(intent);
                }

            }
        });

        lv_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String ,Object> map=permissMyAdapter.listItem.get(position);
                Intent intent=new Intent(HomeActivity.this,PeopleActivity.class);
                intent.putExtra("class_id",map.get("class_id").toString());
                intent.putExtra("tag","0");
                startActivity(intent);
            }
        });


        getAllClass();


    }



    private void getAllClass(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data","getallclass");
                    ApiService.GetString(HomeActivity.this, "getAllClass", paremetes, new RxStringCallback() {
                        @Override
                        public void onNext(Object tag, String response) {

                            String result=response.toString().trim();
                            if (result.contains("#")){
                                String class_data[]=result.split("##");
                                for (int i=0;i<class_data.length;i++){
                                    String class_info[]=class_data[i].split("#");
                                    HashMap<String,Object> map=new HashMap<>();
                                    map.put("class_id",class_info[0]);
                                    map.put("class_name",class_info[1]);
                                    permissMyAdapter.listItem.add(map);
                                }

                                permissMyAdapter.notifyDataSetChanged();
                            }

                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(HomeActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(HomeActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }



    private void getPermissions(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data","pp");
                    ApiService.GetString(HomeActivity.this, "getAllClass", paremetes, new RxStringCallback() {
                        @Override
                        public void onNext(Object tag, String response) {


                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(HomeActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(HomeActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }



    private class PermissMyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局

        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
        /*构造函数*/
        public PermissMyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public int getCount() {

            return listItem.size();//返回数组的长度
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
        /*书中详细解释该方法*/
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final PermissViewHolder holder;
            //观察convertView随ListView滚动情况
            Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_class,null);
                holder = new PermissViewHolder();
                /*得到各个控件的对象*/

                holder.title = (Button) convertView.findViewById(R.id.btn_item_class);

                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (PermissViewHolder)convertView.getTag();//取出ViewHolder对象
            }

            holder.title.setText(listItem.get(position).get("class_name").toString());


            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String ,Object> map=permissMyAdapter.listItem.get(position);
                    Intent intent=new Intent(HomeActivity.this,PeopleActivity.class);

                    System.out.println("班级id："+map.get("class_id"));
                    intent.putExtra("class_id",map.get("class_id").toString());
                    intent.putExtra("tag","0");
                    startActivity(intent);
                }
            });


            return convertView;
        }

    }
    /*存放控件*/
    public final class PermissViewHolder{
        public Button title;


    }



}
