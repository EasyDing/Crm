package com.rm.easy.crm;


import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.rm.easy.crm.jsonBean.JsonGeneral;
import com.rm.easy.crm.client.CreateClient;
import com.rm.easy.crm.client.SelectClient;
import com.rm.easy.crm.iface.HttpCallbackListener;
import com.rm.easy.crm.util.GsonUtil;
import com.rm.easy.crm.util.HttpUtil;
import com.rm.easy.crm.warehouse.AddItem;
import com.rm.easy.crm.warehouse.CreateItem;
import com.rm.easy.crm.warehouse.SelectWarehouse;

import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.VISIBLE;


public class UserLogin extends Activity implements View.OnClickListener {

    public static final int LOGIN_SUCCESS = 0;
    public static final int LOGIN_FAIL = 1;
    public static final int CLICK_CLIENT_MANAGE = 2;
    public static final int CLICK_FINANCING_MANAGE = 3;
    public static final int CONNECT_FAIL = 4;

    Message message = new Message();

    private EditText userName;
    private EditText userPwd;

    private Button orderManage;
    private Button warehouseManage;
    private Button clientManage;
    private Button financingManage;
    private Button sendMsg;
    private Button creatClient;
    private Button renewClient;
    private Button selectClient;
    private Button creatFinancing;
    private Button inventoryAdd;
    private Button inventoryOut;
    private Button selectInventory;
    private Button createInventoryItem;


    private TableRow clientTableRow;
    private TableRow financingTableRow;
    private TableRow warehouseTableRow;

    private String userNameHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        userName = (EditText) findViewById(R.id.uname);
        userPwd = (EditText) findViewById(R.id.upass);


        clientTableRow = (TableRow) findViewById(R.id.client_tablerow);
        financingTableRow = (TableRow) findViewById(R.id.financing_tablerow);
        warehouseTableRow = (TableRow)findViewById(R.id.warehouse_tablerow);

        orderManage = (Button) findViewById(R.id.order);
        //Inventory
        warehouseManage = (Button) findViewById(R.id.warehouse);
        inventoryAdd = (Button)findViewById(R.id.inventory_add);
        inventoryOut = (Button)findViewById(R.id.inventory_out);
        selectInventory = (Button)findViewById(R.id.inventory_select);
        createInventoryItem = (Button)findViewById(R.id.inventory_item_create);
        //Client
        clientManage = (Button) findViewById(R.id.client);
        creatClient = (Button) findViewById(R.id.client_new);
        renewClient = (Button) findViewById(R.id.client_renew);
        selectClient = (Button) findViewById(R.id.client_select);
        financingManage = (Button) findViewById(R.id.financing);
        sendMsg = (Button) findViewById(R.id.submit);

        creatFinancing = (Button) findViewById(R.id.financing_new);
        orderManage.setOnClickListener(this);
        //Inventory
        warehouseManage.setOnClickListener(this);
        inventoryAdd.setOnClickListener(this);
        inventoryOut.setOnClickListener(this);
        selectInventory.setOnClickListener(this);
        createInventoryItem.setOnClickListener(this);

        financingManage.setOnClickListener(this);
        sendMsg.setOnClickListener(this);
        //Client
        clientManage.setOnClickListener(this);
        creatClient.setOnClickListener(this);
        renewClient.setOnClickListener(this);
        selectClient.setOnClickListener(this);
        creatFinancing.setOnClickListener(this);


    }


    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_SUCCESS:
                    //登录成功显示管理按钮
                    String successRes = (String) msg.obj;
                    orderManage.setVisibility(VISIBLE);
                    warehouseManage.setVisibility(VISIBLE);
                    clientManage.setVisibility(VISIBLE);
                    financingManage.setVisibility(VISIBLE);
                    Toast.makeText(UserLogin.this, "登录成功", Toast.LENGTH_SHORT).show();
                    setUserNameHolder(userName.getText().toString());
                    break;
                case LOGIN_FAIL:
                    String failRes = (String) msg.obj;

                    Toast.makeText(UserLogin.this, failRes, Toast.LENGTH_SHORT).show();
                    break;
                case CONNECT_FAIL:
                    //判断网络链接情况，无返回表示网络不联通，进行重新握手；联通以后进行判断.
                    Toast.makeText(UserLogin.this, "建立连接中，请稍候", Toast.LENGTH_SHORT).show();
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Log.i("UserLogin", "Reconnecting");
                            String address = "http://rmcoffee.imwork.net/user/select_user.php";
                            String reqStr = "uname=" + userName.getText() + "&upwd=" + userPwd.getText();
                            sendRequest(address, reqStr);
                            this.cancel();
                        }
                    }, 5000);
                    break;
            }
        }

    };

    //处理按钮点击
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.submit:
                Log.i("UserLogin", "Click Submit");
                Toast.makeText(UserLogin.this, "建立连接中，请稍候", Toast.LENGTH_SHORT).show();
                String address = "http://rmcoffee.imwork.net/user/select_user.php";
                String reqStr = "uname=" + userName.getText() + "&upwd=" + userPwd.getText();
                sendRequest(address, reqStr);
                break;
            case R.id.client:
                Log.i("UserLogin", "Click Client Manage");
                financingTableRow.setVisibility(View.GONE);
                warehouseTableRow.setVisibility(View.GONE);
                clientTableRow.setVisibility(VISIBLE);
                break;
            case R.id.client_new:
                Log.i("UserLogin", "Click Create Client");
                Intent createClientIntent = new Intent(UserLogin.this, CreateClient.class);
                startActivity(createClientIntent);
                break;
            case R.id.client_select:
                Log.i("UserLogin", "Click Select Client");
                Intent selectClientIntent = new Intent(UserLogin.this, SelectClient.class);
                startActivity(selectClientIntent);
                break;
            case R.id.financing:
                Log.i("UserLogin", "Click Financing Manage");
                clientTableRow.setVisibility(View.GONE);
                warehouseTableRow.setVisibility(View.GONE);
                financingTableRow.setVisibility(VISIBLE);
                break;
            case R.id.warehouse:
                Log.i("UserLogin", "Click Warehouse Manage");
                clientTableRow.setVisibility(View.GONE);
                financingTableRow.setVisibility(View.GONE);
                warehouseTableRow.setVisibility(VISIBLE);
                break;
            case R.id.inventory_add:
                Log.i("UserLogin", "Click Inventory Add");
                Intent inventoryAdd = new Intent(UserLogin.this, AddItem.class);
                startActivity(inventoryAdd);
                break;
            case R.id.inventory_out:
                Log.i("UserLogin", "Click Inventory Out");
                break;
            case R.id.inventory_select:
                Log.i("UserLogin", "Click Inventory Select");
                Intent selectWarehouseIntent = new Intent(UserLogin.this, SelectWarehouse.class);
                startActivity(selectWarehouseIntent);
                break;
            case R.id.inventory_item_create:
                Log.i("UserLogin", "Click Create Item");
                Intent createInventoryItemIntent = new Intent(UserLogin.this, CreateItem.class);
                startActivity(createInventoryItemIntent);
                break;

        }

    }


    private void sendRequest(String address, String reqStr) {
        HttpUtil.sendHttpRequest(address, reqStr, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.i("UserLogin", response);
                JsonGeneral jsonGenerals = new GsonUtil().parseJsonWithGson(response, JsonGeneral.class);
                Log.i("UserLogin", jsonGenerals.getStatus());
                Message msg = new Message();
                //此处判断服务器的响应，成功返回Success，失败返回Fail
                if (jsonGenerals.getStatus().equals("Success")) {
                    msg.what = LOGIN_SUCCESS;
                } else {
                    msg.what = LOGIN_FAIL;
                }
                msg.obj = jsonGenerals.getStatus();
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                Message msg = new Message();
                msg.what = CONNECT_FAIL;
                handler.sendMessage(msg);
                e.printStackTrace();

            }
        });
    }

    public String getUserNameHolder() {
        return userNameHolder;
    }

    public void setUserNameHolder(String userNameHolder) {
        this.userNameHolder = userNameHolder;
    }
}
