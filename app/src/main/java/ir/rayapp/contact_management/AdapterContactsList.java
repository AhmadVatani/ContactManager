package ir.rayapp.contact_management;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class AdapterContactsList extends ArrayAdapter<ObjectContact> {

    private Activity activity;
    boolean isSdcardContacts;
    ArrayList<ObjectContact> contacts;
    private LayoutInflater inflater;

    AdapterContactsList(Activity activity, boolean isSdcardContacts, ArrayList<ObjectContact> contacts) {
        super(activity.getApplicationContext(), R.layout.row_contact, contacts);

        this.activity = activity;
        this.isSdcardContacts = isSdcardContacts;
        this.contacts = contacts;
        inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder {
        LinearLayout ll;
        TextView txtName;
        TextView txtNumber;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        final ViewHolder holder;
        final ObjectContact curItem = getItem(position);

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.row_contact, parent, false);
            holder.ll = (LinearLayout) view.findViewById(R.id.ll);
            holder.txtName = (TextView) view.findViewById(R.id.txtName);
            holder.txtNumber = (TextView) view.findViewById(R.id.txtNumber);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.txtName.setText(curItem.name);
        holder.txtNumber.setText(curItem.number);
        if(isSdcardContacts) {
            holder.ll.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (Util.deleteContact(curItem.number, curItem.name)) {
                        contacts.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(Util.context, "Contact deleted!", Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
            });

            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(activity);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_add_contact);
                    final EditText etFName = (EditText) dialog.findViewById(R.id.etFName);
                    final EditText etLName = (EditText) dialog.findViewById(R.id.etLName);
                    final EditText etNumber = (EditText) dialog.findViewById(R.id.etNumber);

                    if(curItem.name.split("\\s+").length > 0)
                        etFName.setText(curItem.name.split("\\s+")[0]);
                    if(curItem.name.split("\\s+").length > 1)
                        etLName.setText(curItem.name.split("\\s+")[1]);
                    etNumber.setText(curItem.number);

                    Button btn = (Button) dialog.findViewById(R.id.btnSave);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String name = etFName.getText().toString() + " " + etLName.getText().toString();
                            String mobile = etNumber.getText().toString();
                            if(Util.updateContact(name, mobile, contacts.get(position).id)) {
                                contacts.get(position).name = name;
                                contacts.get(position).number = mobile;
                                notifyDataSetChanged();
                                Toast.makeText(Util.context, "Contact Updated!", Toast.LENGTH_LONG).show();
                            }
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
        }

        return view;
    }
}
