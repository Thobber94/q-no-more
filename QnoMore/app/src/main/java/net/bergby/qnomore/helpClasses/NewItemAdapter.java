package net.bergby.qnomore.helpClasses;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
    private final double[] sum = new double[1];
    private final ArrayList<String> items = new ArrayList<>();

    public interface NewItemAdapterInterface
    {
        void onClick(Double sum, ArrayList<String> item);
    }

    private NewItemAdapterInterface adapterInterface;

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

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        final SharedPreferences.Editor editor = preferences.edit();

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
        String currency = "€";

        int oldQuantity = preferences.getInt("quantity" + position, 0);

        holder.textPrice.setText(currency + String.valueOf(newItem.getPrice()));
        holder.textTitle.setText(newItem.getTitle());
        holder.addItem.setTag(position);
        holder.subItem.setTag(position);
        holder.quantity.setTag(position);

        holder.quantity.setText(String.valueOf(preferences.getInt("quantity" + position, 0)));
        newItem.setQuantity(preferences.getInt("quantity" + position, 0));

        // Updates the item array if it should be items in it
        if (oldQuantity != 0)
        {
            for (int i = 0; i < oldQuantity; i++)
            {
                sum[0] += newItem.getPrice();
                items.add(newItem.getTitle());
            }
        }

        holder.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantityFromItem = newItem.getQuantity();

                // Adds 1 to the counter
                newItem.setQuantity(quantityFromItem + 1);
                holder.quantity.setText(String.valueOf(newItem.getQuantity()));
                sum[0] += newItem.getPrice();
                items.add(newItem.getTitle());
                adapterInterface.onClick(sum[0], items);

                editor.putInt("quantity" + position, newItem.getQuantity()).apply();
            }
        });

        holder.subItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantityFromItem = newItem.getQuantity();
                // Subtract 1 from counter
                if (quantityFromItem > 0)
                {
                    newItem.setQuantity(quantityFromItem - 1);
                    holder.quantity.setText(String.valueOf(newItem.getQuantity()));
                    sum[0] -=  newItem.getPrice();
                    items.remove(newItem.getTitle());
                    adapterInterface.onClick(sum[0], items);

                    editor.putInt("quantity" + position, newItem.getQuantity()).apply();
                }
            }
        });

        return convertView;
    }

}
