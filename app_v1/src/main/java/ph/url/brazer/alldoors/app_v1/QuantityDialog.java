package ph.url.brazer.alldoors.app_v1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by User on 27.03.2014.
 */
public class QuantityDialog extends DialogFragment {

    private ProductNode product;

    public QuantityDialog(ProductNode product) { this.product = product; }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View v = inflater.inflate(R.layout.dialog_quantity, null);
        builder.setView(v)
                .setTitle("Количество")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText input = (EditText) v.findViewById(R.id.editTextQuantity);
                        String str = input.getText().toString();
                        int quantity = 0;
                        try {
                            quantity = Integer.parseInt(str);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        product.setQuantity(quantity);
                        CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBoxInstall);
                        product.setInstalled(checkBox.isChecked());
                    }
                });

        return builder.create();
    }
}
