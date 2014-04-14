package ph.url.brazer.alldoors.app_v1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by brazer on 15.3.14.
 */
public class OptionDialog extends DialogFragment{

    private SparseBooleanArray mSparseBooleanArray;
    CompoundButton button;
    private int tag;
    private ProductNode product;
    private ArrayList<Option> options = new ArrayList<Option>();
    private OptionLeaf optionLeaf;

    private class Option {
        String name;
        ArrayList<String> values = new ArrayList<String>();
        ArrayList<Double> addedPrices = new ArrayList<Double>();
    }

    public OptionDialog(SparseBooleanArray mSparseBooleanArray, CompoundButton buttonView) {
        this.mSparseBooleanArray = mSparseBooleanArray;
        tag = (Integer) buttonView.getTag();
        button = buttonView;
        product = TreeOrder.getRoot().getCurProduct();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final String[] list = getList(product.getId());
        boolean bln[] = new boolean[list.length];

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        AlertDialog.Builder builder1 = builder.setView(inflater.inflate(R.layout.dialog_option, null))
                .setTitle("Опции")
                .setMultiChoiceItems(list, bln, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        optionLeaf = product.getOption(which);
                        ArrayList<String> listValues = options.get(which).values;
                        ArrayList<Double> listPrices = options.get(which).addedPrices;
                        String list[] = new String[listValues.size()];
                        for (int i = 0; i < list.length; i++)
                            list[i] = listValues.get(i) + " (+" + Math.round(listPrices.get(i)) + ")";
                        ValueDialog valueDialog = new ValueDialog(list, listValues, listPrices);
                        valueDialog.show(getFragmentManager(), Integer.toString(which));
                    }
                })
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mSparseBooleanArray.put(tag, true);
                        QuantityDialog quantityDialog = new QuantityDialog(product);
                        quantityDialog.show(getFragmentManager(), null);
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mSparseBooleanArray.put(tag, false);
                        button.setChecked(false);
                        ClientRoot client = TreeOrder.getRoot();
                        client.deleteProduct(product.getId());
                    }
                });
        return builder.create();
    }

    private String[] getList(String id) {
        UrlTask task = new UrlTask();
        JSONResult.Parameter.setUrl(MainActivity.SITE_URL+"getOptions.php");
        JSONResult.Parameter params[] = new JSONResult.Parameter[1];
        params[0] = new JSONResult.Parameter();
        params[0].name = "IdProductItem";
        params[0].value = id;
        task.execute(params);
        String res = "";
        try {
            res = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.i("Result", res);
        ArrayList<String> listOptions, listValues, listAddedPrices;
        listOptions = task.getList("OptionName");
        listValues = task.getList("Value");
        listAddedPrices = task.getList("PriceAdded");
        return setOptionsAndGetList(listOptions, listValues, listAddedPrices);
    }

    private String[] setOptionsAndGetList(ArrayList<String> listOptions,
                                          ArrayList<String> listValues,
                                          ArrayList<String> listAddedPrices)
    {
        String name = "";
        Option opt = null;
        for (int i=0; i<listOptions.size(); i++) {
            if (!name.equals(listOptions.get(i))) {
                if (opt!=null) options.add(opt);
                name = listOptions.get(i);
                opt = new Option();
                opt.name = listOptions.get(i);
            }
            assert opt != null;
            Double p = Double.valueOf(listAddedPrices.get(i));
            opt.values.add(listValues.get(i));
            opt.addedPrices.add(p);
        }
        options.add(opt);
        return getListFromOptionsAndAddLeaves();
    }

    private String[] getListFromOptionsAndAddLeaves() {
        String list[] = new String[options.size()];
        for (int i=0; i<list.length; i++) {
            list[i] = options.get(i).name;
            OptionLeaf leaf = new OptionLeaf(list[i], "неопределено", 0);
            product.addOption(leaf);
        }
        return list;
    }

    class ValueDialog extends DialogFragment {

        private String list[];
        private ArrayList<String> values;
        private ArrayList<Double> prices;

        public ValueDialog(String list[], ArrayList<String> values, ArrayList<Double> prices) {
            this.list = list;
            this.values = values;
            this.prices = prices;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            final ChoiceListener listener = new ChoiceListener();
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.dialog_option, null))
                    .setTitle("Значения")
                    .setSingleChoiceItems(list, list.length - 1, listener)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            optionLeaf.setValue(values.get(listener.position));
                            optionLeaf.setAddedPrice(prices.get(listener.position));
                        }
                    });
            return builder.create();
        }

        private class ChoiceListener implements DialogInterface.OnClickListener {

            int position;

            @Override
            public void onClick(DialogInterface dialog, int which) {
                position = which;
            }
        }

    }

}
