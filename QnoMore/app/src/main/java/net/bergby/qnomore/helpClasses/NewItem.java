package net.bergby.qnomore.helpClasses;

import org.json.JSONObject;

/**
 * Created by thomas on 11-Apr-17.
 */
public class NewItem
{
    private int imageId;
    private String title;
    private JSONObject menu;
    private double price;
    private int quantity;
    private double totalSum;

    public NewItem(int imageId, String title, JSONObject menu, double price)
    {
        this.imageId = imageId;
        this.title = title;
        this.menu = menu;
        this.price = price;
    }

    public int getImageId()
    {
        return imageId;
    }

    public String getTitle()
    {
        return title;
    }

    public JSONObject getMenu()
    {
        return menu;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public double getTotalSum()
    {
        return totalSum;
    }

    public void setImageId(int imageId)
    {
        this.imageId = imageId;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setMenu(JSONObject menu)
    {
        this.menu = menu;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public void setTotalSum(float totalSum)
    {
        this.totalSum = totalSum;
    }

    @Override
    public String toString()
    {
        return title + "\n" + quantity;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(float price)
    {
        this.price = price;
    }
}
