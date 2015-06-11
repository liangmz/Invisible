package com.Invisible.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.Invisible.main.R;

public class LogOn extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logon);
        bind();
    }

    private void bind(){
        Button btn_Exit = (Button)findViewById(R.id.btn_logon_exit);
        btn_Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });

        Button btn_logon = (Button)findViewById(R.id.btn_logon_logon);
        btn_logon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(LogOn.this,"LogOn",Toast.LENGTH_SHORT).show();

                Intent it = new Intent(LogOn.this,Link.class);
                Bundle bd = new Bundle();
                EditText et = (EditText)findViewById(R.id.et_ip);

                bd.putString("ip",et.getText().toString());//服务器ip
                bd.putInt("port",12356);//服务器端口

                it.putExtras(bd);
                startActivityForResult(it,1);
            }
        });
    }

}
