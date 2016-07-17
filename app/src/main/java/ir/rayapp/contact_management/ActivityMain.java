package ir.rayapp.contact_management;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class ActivityMain extends Activity implements View.OnClickListener {

    Button btnInsert;
    Button btnShowSim;
    Button btnshowsdcard;
    ListView lv;
    TextView txtNothing;
    AdapterContactsList adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.context = getApplicationContext();
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        btnInsert = (Button) findViewById(R.id.btninsert);
        btnShowSim = (Button) findViewById(R.id.btnshowsim);
        btnshowsdcard = (Button) findViewById(R.id.btnshowsdcard);
        lv = (ListView) findViewById(R.id.lv);
        txtNothing = (TextView) findViewById(R.id.txtNothing);

        btnInsert.setOnClickListener(this);
        btnShowSim.setOnClickListener(this);
        btnshowsdcard.setOnClickListener(this);

        ArrayList<ObjectContact> contacts = Util.getSdcardContacts();
        if(contacts.size()>0) {
            adapter = new AdapterContactsList(ActivityMain.this, true, contacts);
            lv.setAdapter(adapter);
            txtNothing.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
        } else {
            txtNothing.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == btnshowsdcard) {
            ArrayList<ObjectContact> contacts = Util.getSdcardContacts();
            if(contacts.size()>0) {
                adapter = new AdapterContactsList(ActivityMain.this, true, contacts);
                lv.setAdapter(adapter);
                txtNothing.setVisibility(View.GONE);
                lv.setVisibility(View.VISIBLE);
            } else {
                txtNothing.setVisibility(View.VISIBLE);
                lv.setVisibility(View.GONE);
            }
        }
        else if(v == btnShowSim) {
            ArrayList<ObjectContact> contacts = Util.getSimContacts();
            if(contacts.size()>0) {
                adapter = new AdapterContactsList(ActivityMain.this, false, contacts);
                lv.setAdapter(adapter);
                txtNothing.setVisibility(View.GONE);
                lv.setVisibility(View.VISIBLE);
            } else {
                txtNothing.setVisibility(View.VISIBLE);
                lv.setVisibility(View.GONE);
            }
        }
        if(v == btnInsert) {
            final Dialog dialog = new Dialog(ActivityMain.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_add_contact);
            final EditText etFName = (EditText) dialog.findViewById(R.id.etFName);
            final EditText etLName = (EditText) dialog.findViewById(R.id.etLName);
            final EditText etNumber = (EditText) dialog.findViewById(R.id.etNumber);
            Button btn = (Button) dialog.findViewById(R.id.btnSave);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = etFName.getText().toString() + " " + etLName.getText().toString();
                    String mobile  = etNumber.getText().toString();
                    Util.AddContact(name, mobile);
                    Toast.makeText(Util.context, "Contact Inserted!", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
}
