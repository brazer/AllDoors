package ph.url.brazer.alldoors.app_v1;

import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import java.util.ArrayList;

/**
 * Created by User on 17.03.14.
 */
public class SingleSelectionAdapter<T> extends BaseAdapter{

    Context mContext;
    LayoutInflater mInflater;
    ArrayList<T> mList;
    SparseBooleanArray mSparseBooleanArray;
    FragmentManager fragmentManager;

    public void setFragmentManager(FragmentManager manager) {
        fragmentManager = manager;
    }

    public SparseBooleanArray getmSparseBooleanArray() {
        return mSparseBooleanArray;
    }

    public SingleSelectionAdapter(Context context, ArrayList<T> list, SingleSelectionAdapter<T> adapterStateSaved) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        if (adapterStateSaved!=null) {
            mSparseBooleanArray = adapterStateSaved.getmSparseBooleanArray();
        } else {
            mSparseBooleanArray = new SparseBooleanArray();
            for (int i=0; i<list.size(); i++) { mSparseBooleanArray.put(i, false); }
        }
        mList = new ArrayList<T>();
        this.mList = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.row_clients, null);
        }
        RadioButton mRadioButton = (RadioButton) convertView.findViewById(R.id.radioButton);
        mRadioButton.setText(((Item) mList.get(position)).getName());
        mRadioButton.setTag(position);
        boolean isChecked = mSparseBooleanArray.get(position);
        mRadioButton.setChecked(isChecked);
        mRadioButton.setOnCheckedChangeListener(mCheckedChangeListener);
        if (isChecked) {
            Log.i("SingleSelectionAdapter.getView()", "mRadioButton " + mRadioButton.getText() + " is checked");
            MainActivity.clients.curButton = mRadioButton;
        }
        return convertView;
    }

    CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.isPressed()) {
                if (MainActivity.clients.curButton != null) {
                    int position = (Integer) MainActivity.clients.curButton.getTag();
                    MainActivity.clients.curButton.setChecked(false);
                    mSparseBooleanArray.put((Integer) MainActivity.clients.curButton.getTag(), false);
                    Log.i("SingleSelectionAdapter.getCheckChanged()", "button " + MainActivity.clients.curButton.getText() + " is unchecked");
                    Log.i("mSparseBooleanArray of button", Boolean.toString(mSparseBooleanArray.get(position)));
                }
                MainActivity.clients.curButton = (RadioButton) buttonView;
                mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
                ClientRoot client = TreeOrder.getRoot();
                Item item = (Item) mList.get((Integer) buttonView.getTag());
                client.setId(item.getId());
                Log.i("SingleSelectionAdapter.getCheckChanged()", "buttonView " + buttonView.getText() + " is pressed");
                Log.i("mSparseBooleanArray of buttonView", Boolean.toString(mSparseBooleanArray.get((Integer) buttonView.getTag())));
            } else {
                Log.i("SingleSelectionAdapter.getCheckChanged()", "buttonView " + buttonView.getText() + " is not pressed");
                Log.i("mSparseBooleanArray of buttonView", Boolean.toString(mSparseBooleanArray.get((Integer) buttonView.getTag())));
            }
        }
    };


}
