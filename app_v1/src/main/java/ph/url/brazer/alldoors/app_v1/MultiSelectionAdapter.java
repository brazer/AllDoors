package ph.url.brazer.alldoors.app_v1;


import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import java.util.ArrayList;

public class MultiSelectionAdapter<T> extends BaseAdapter{

    Context mContext;
    LayoutInflater mInflater;
    ArrayList<T> mList;
    SparseBooleanArray mSparseBooleanArray;

    FragmentManager fragmentManager;
    public void setFragmentManager(FragmentManager manager) {
        fragmentManager = manager;
    }

    public MultiSelectionAdapter(Context context, ArrayList<T> list, MultiSelectionAdapter<T> adapterStateSaved) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        if (adapterStateSaved!=null) {
            mSparseBooleanArray = adapterStateSaved.getmSparseBooleanArray();
        }
        else {
            mSparseBooleanArray = new SparseBooleanArray();
        }
        mList = new ArrayList<T>();
        this.mList = list;
    }

    public SparseBooleanArray getmSparseBooleanArray() {
        return mSparseBooleanArray;
    }

    public ArrayList<T> getCheckedItems() {
        ArrayList<T> mTempArry = new ArrayList<T>();
        for(int i=0;i<mList.size();i++) {
            if(mSparseBooleanArray.get(i)) {
                mTempArry.add(mList.get(i));
            }
        }
        return mTempArry;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.row, null);
        }
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        Item item = (Item) mList.get(position);
        tvTitle.setText(item.getName()+" - "+item.getPrice()+" у.е.");
        CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.chkEnable);
        mCheckBox.setTag(position);
        mCheckBox.setChecked(mSparseBooleanArray.get(position));
        mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
        return convertView;
    }

    OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.isPressed()) {
                int position = (Integer) buttonView.getTag();
                ClientRoot client = TreeOrder.getRoot();
                Item item = (Item) getItem(position);
                if (!mSparseBooleanArray.get(position)) {
                    ProductNode product = new ProductNode(item.getId(), item.getPrice());
                    client.addProductAndSetCurrentProduct(product);
                    OptionDialog optionDialog =
                            new OptionDialog(mSparseBooleanArray, buttonView);
                    try {
                        optionDialog.show(fragmentManager, null);
                    } catch (Exception ex) {
                        Log.e("MultiSelectionAdapter.onCheckedChanged", ex.toString());
                    }
                } else {
                    mSparseBooleanArray.put(position, isChecked);
                    client.deleteProduct(item.getId());
                }
            }
        }
    };

}