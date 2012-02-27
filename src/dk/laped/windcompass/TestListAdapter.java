package dk.laped.windcompass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TestListAdapter extends ArrayAdapter<TestObject> {

	private Context context;
	private int viewResourceId;
	private LayoutInflater inflater;

	public TestListAdapter(Context context) {
		super(context, R.layout.list_item);
		this.context = context;
		this.viewResourceId = R.layout.list_item;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(viewResourceId, null);

			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.list_name);
			holder.windCompass = (WindDirectionCompass) convertView
					.findViewById(R.id.list_compass);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		TestObject item = getItem(position);
		
		holder.name.setText(item.name);
		holder.windCompass.setWindDirections(item.windDirections);
		holder.windCompass.setIsPerfectWindDirection(item.isPerfectWindDirection);
		holder.windCompass.setCurrentWindDegree(item.currentWindDirection);

		return convertView;
	}

	class ViewHolder {
		TextView name;
		WindDirectionCompass windCompass;
	}
}
