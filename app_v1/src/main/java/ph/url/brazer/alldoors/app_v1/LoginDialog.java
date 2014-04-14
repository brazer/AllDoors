package ph.url.brazer.alldoors.app_v1;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by User on 12.03.14.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LoginDialog extends DialogFragment {

    private final String LOGIN = "brazer";
    private final String PASSWORD = "104611";

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        System.exit(0);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View v = inflater.inflate(R.layout.dialog_signin, null);
        builder.setView(v)
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText log = (EditText) v.findViewById(R.id.username);
                        String strLogin = log.getText().toString();
                        EditText psw = (EditText) v.findViewById(R.id.password);
                        String strPasw = psw.getText().toString();
                        /*if (!strLogin.equals(LOGIN) | !strPasw.equals(PASSWORD))
                            (new LoginDialog()).show(getFragmentManager(), null);*/
                    }
                })
                .setNegativeButton("Выход", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.exit(0);
                    }
                });
        return builder.create();
    }

}
