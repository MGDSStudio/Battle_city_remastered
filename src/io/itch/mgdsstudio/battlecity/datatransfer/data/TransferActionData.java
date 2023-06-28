package io.itch.mgdsstudio.battlecity.datatransfer.data;

public class TransferActionData {
	//public final
	public final static int ACTIVATED = 1;
	public final static int DEACTIVATED = 0;
    private boolean used;
    private char prefix;
    private int value;

	public TransferActionData(char prefix, int value)
	{
		recreate(prefix, value);
	}
	
	public void recreate(char prefix, int value)
	{
		this.prefix = prefix;
		this.value = value;
		used = false;
	}

	public void setUsed(boolean used)
	{
		this.used = used;
	}

	public boolean isUsed()
	{
		return used;
	}

	public void setPrefix(char prefix)
	{
		this.prefix = prefix;
	}

	public char getPrefix()
	{
		return prefix;
	}

	public void setValue(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}
    
    
}
