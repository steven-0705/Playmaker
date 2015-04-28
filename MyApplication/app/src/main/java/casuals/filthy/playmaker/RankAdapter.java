package casuals.filthy.playmaker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.NumberPicker;

import java.util.Arrays;

/**
 * Created by Chris on 4/27/2015.
 */
public class RankAdapter extends ArrayAdapter<String> {

    public static final String[] places = {"1st", "2nd", "3rd", "4th" , "5th",
            "6th", "7th", "8th", "9th", "10th", "11th", "14th", "15th",
            "16th", "17th", "18th", "19th", "20th", "21st", "22nd", "23rd",
            "24th", "25th", "26th", "27th", "28th", "29th", "30th" };

    private int length = places.length;

    public RankAdapter(Context context, int resource, int textView, String[] objects, int length) {
        super(context, resource, textView, objects);

        this.length = length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);

        NumberPicker np = (NumberPicker) v.findViewById(R.id.rankPicker);
        np.setDisplayedValues(Arrays.copyOfRange(places, 0, length));
        np.setMinValue(1);
        np.setMaxValue(length);
        np.setValue(length);

        return v;

    }
}
