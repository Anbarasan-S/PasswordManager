import java.util.Scanner;
public class Test 
{
	public static void main(String args[])
	{
		Scanner sc=new Scanner(System.in);
		String s=sc.nextLine();
		int num_ptr=0,sym_ptr=-1,count=0,val=0;
		int ind;
		for(ind=0;ind<s.length();ind++)
		{
			char ch=s.charAt(ind);
			if(ch>='0'&&ch<='9')
			{
				val=ch-48;
				break;
			}
		}
		
		for(ind=ind+1;ind<s.length();ind+=1)
		{
			char ch=s.charAt(ind);
			
			if(ch>='0'&&ch<='9')
			{
				
				char charc=' ';
				for(sym_ptr=sym_ptr+1;sym_ptr<s.length();sym_ptr+=1)
				{
					charc=s.charAt(sym_ptr);
					if(charc=='+'||charc=='-'||charc=='*'||charc=='/')
					{
						break;
					}
				}
				
					if(charc=='+')
					{
						val+=(ch-48);
					}
					else if(charc=='-')
					{
						val-=ch-48;
					}
					else if(charc=='*')
					{
						val*=(ch-48);
					}
					else if(charc=='/')
					{
						val/=(ch-48);
					}
					System.out.println(val);
			}
		}
		System.out.println(val);
	}
}
