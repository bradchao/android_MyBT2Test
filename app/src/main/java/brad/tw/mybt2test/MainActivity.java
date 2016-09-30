package brad.tw.mybt2test;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 1;

    private BluetoothAdapter mBluetoothAdapter;
    private boolean isSupport = true;
    private boolean isBTInitEnable = false, isBTEnable = false;

    private ListView listDevices;
    private SimpleAdapter adapter;
    private String[] from = {"name","addr","type"};
    private int[] to = {R.id.item_name,R.id.item_addr,R.id.item_type};
    private LinkedList<HashMap<String,String>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupBT();

        listDevices = (ListView)findViewById(R.id.listDevices);
        initListView();
    }

    private void initListView(){
        data = new LinkedList<>();
        adapter = new SimpleAdapter(this,data,R.layout.layout_item,from,to);
        listDevices.setAdapter(adapter);
    }

    public void setupBT(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            isSupport = false;
        }else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }else{
                isBTInitEnable = true;
                isBTEnable = true;
            }
        }
    }

    public void queryPaired(View v){
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices){
            HashMap<String,String> item = new HashMap<>();
            item.put(from[0], device.getName());
            item.put(from[1], device.getAddress());
            item.put(from[2], "paired");
            data.add(item);
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT){
            if (resultCode == RESULT_OK){
                isBTEnable = true;
            }
        }
    }

    @Override
    public void finish() {
        if (isSupport && !isBTInitEnable){
            mBluetoothAdapter.disable();
        }
        super.finish();
    }
}
