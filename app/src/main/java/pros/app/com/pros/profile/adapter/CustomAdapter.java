package pros.app.com.pros.profile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import pros.app.com.pros.R;

public class CustomAdapter extends BaseAdapter {

    Context context;
    String[] countTypeList;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, String[] countTypeList) {
        this.context = applicationContext;
        this.countTypeList = countTypeList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return countTypeList.length;
    }

    @Override
    public Object getItem(int position) {
        return countTypeList[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.item_dropdown, null);
        TextView names = (TextView) convertView.findViewById(R.id.content_name);
        names.setText(countTypeList[position]);
        return convertView;
    }
}
