package net.bergby.qnomore.helpClasses;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import net.bergby.qnomore.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomas on 11-Apr-17.
 */
public class NewItemAdapter extends ArrayAdapter<NewItem>
{
    private Context context;
    private String currency = "€";
    private final double[] sum = new double[1];
    private final ArrayList<String> items = new ArrayList<>();

    public interface NewItemAdapterInterface
    {
        void onClick(Double sum, ArrayList<String> item);
    }

    NewItemAdapterInterface adapterInterface;

    public  NewItemAdapter(Context context, int resourceId,
                           List<NewItem> items, NewItemAdapterInterface adapterInterface)
    {
        super(context, resourceId, items);
        this.context = context;
        this.adapterInterface = adapterInterface;
        //this.quantityArray = quantityArray;
    }

    private class ViewHolder
    {
        ImageView imageView;
        TextView textTitle;
        TextView textPrice;
        Button addItem, subItem;
        TextView quantity;
    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {

        final ViewHolder holder;
        final NewItem newItem = getItem(position);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.menu_text_view, null);
            holder = new ViewHolder();
            holder.textTitle = (TextView) convertView.findViewById(R.id.item);
            holder.textPrice = (TextView) convertView.findViewById(R.id.bottom_text);

            holder.addItem = (Button) convertView.findViewById(R.id.button1);
            holder.subItem = (Button) convertView.findViewById(R.id.button2);
            holder.quantity = (TextView) convertView.findViewById(R.id.quantity_text_view);
        }

        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        convertView.setTag(holder);
        holder.textPrice.setText(currency + String.valueOf(newItem.getPrice()));
        holder.textTitle.setText(newItem.getTitle());
        //holder.imageView.setImageResource(newItem.getImageId());
        holder.addItem.setTag(position);
        holder.subItem.setTag(position);
        holder.quantity.setTag(position);
        final int[] counter = {0};
        holder.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Adds 1 to the counter
                counter[0] = counter[0] + 1;
                holder.quantity.setText(String.valueOf(counter[0]));
                sum[0] += newItem.getPrice();
                items.add(newItem.getTitle());
                adapterInterface.onClick(sum[0], items);
            }
        });

        holder.subItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Subtract 1 from counter
                if (counter[0] > 0)
                {
                    counter[0] = counter[0] - 1;
                    holder.quantity.setText(String.valueOf(counter[0]));
                    sum[0] -=  newItem.getPrice();
                    items.remove(newItem.getTitle());
                    adapterInterface.onClick(sum[0], items);
                }
            }
        });

        return convertView;
    }

}
