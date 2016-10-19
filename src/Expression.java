import java.io.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
public class Expression {
	
	int VariableNumber;
	int TermNumber;
    int[][] VariableCount = new int[10][10];
	ArrayList Variable = new ArrayList();
	int[] Factor = new int[10];
	
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		int flag = 0;
		Expression expr = new Expression();
		while(true)
		{
			InputStreamReader isr = new InputStreamReader(System.in); 
			BufferedReader bf = new BufferedReader(isr);
			try{
				
				String str = bf.readLine();//input by users
				//match
				String reg1 = "!simplify";
				String reg2 = "!d/d";
				Pattern pat1 = Pattern.compile(reg1);
				Matcher mat1 = pat1.matcher(str);
				Pattern pat2 = Pattern.compile(reg2);
				Matcher mat2 = pat2.matcher(str);
				//deal
				if(mat1.find())
				{
					//showexpr(expr);
					if(flag == 0)
						System.out.println("Error!");
					else
					{
						expr = simplify(str,expr);
						showexpr(expr);
					}
					
				}else if(mat2.find())
				{
					if(flag == 0)
						System.out.println("Error!");
					else
						expr = derivative(str,expr);
					showexpr(expr);
				}else
				{
					expr = deal(str,expr);
					flag = 1;
					showexpr(expr); 
				}
			}catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace(); 
			}		
		}
	}

	public static Expression deal(String str,Expression ori)
	{
		long a=System.currentTimeMillis();
		System.out.println("始于："+a);
		int state = 0;//自动机状态
			//System.out.println(str);
			for(int i = 0 ; i < str.length(); i ++)//对输入的表达式进行合法性判断
			{
				switch(state)
				{
				case 0://初始状态下的转移函数
				{
					if(str.charAt(i) >= 48 && str.charAt(i) <= 57)
					{
						state = 1;
					}else if(str.charAt(i) >= 97 && str.charAt(i) <= 122)
					{
						state = 2;
					}else if(str.charAt(i) == 43 || str.charAt(i) == 42)
					{
						state = 3;
					}else
					{
						state = 3;
					}
					break;		
				}
				case 1://尾数为数字时的转移函数
				{
					if(str.charAt(i) >= 48 && str.charAt(i) <= 57)
					{
						state = 1;
					}else if(str.charAt(i) >= 97 && str.charAt(i) <= 122)
					{
						state = 3;
					}else if(str.charAt(i) == 43 || str.charAt(i) == 42)
					{
						state = 4;
					}else
					{
						state = 3; 
					}
					break;	
				}
				case 2://尾数为变量时的转移函数
				{
					if(str.charAt(i) >= 48 && str.charAt(i) <= 57)
					{
						state = 3;
					}else if(str.charAt(i) >= 97 && str.charAt(i) <= 122)
					{
						state = 3;
					}else if(str.charAt(i) == 43 || str.charAt(i) == 42)
					{
						state = 4;
					}else
					{
						state = 3;
					}		
					break;	
				}
				case 3://非法状态
				{
					break;	
				}
				case 4://尾数为符号时的转移函数
				{
					if(str.charAt(i) >= 48 && str.charAt(i) <= 57)
					{
						state = 1;
					}else if(str.charAt(i) >= 97 && str.charAt(i) <= 122)
					{  
						state = 2;
					}else if(str.charAt(i) == 43 || str.charAt(i) == 42)
					{
						state = 3;
					}else
					{
						state = 3;
					}
					break;		
				}
				default:
					break;
				}
				//System.out.println(state);
				if(state == 3)
				{
					System.out.println("Error!");
					System.out.println("终于："+System.currentTimeMillis());
					System.out.println("执行耗时 : "+(System.currentTimeMillis()-a)+" 毫秒 ");
					return ori;
				}			
			}
			if(state == 1 || state == 2)
			{
				//System.out.println(str);
			}else if(state == 4)
			{
				System.out.println("Error!");
				System.out.println("终于："+System.currentTimeMillis());
				System.out.println("执行耗时 : "+(System.currentTimeMillis()-a)+" 毫秒 ");
				return ori;
			}
			//形成数据结构
			Expression Expr = new Expression();
			for(int i = 0; i < 10; i ++)
			{
				Expr.Factor[i] = 1;
			}
			//System.out.println(Expr.TermNumber);
			//System.out.println(Expr.VariableNumber);
			state = 0;//清空状态位
			for(int i = 0; i < str.length(); i ++)//形成数据结构的自动机
			{
				if(str.charAt(i) >= 48 && str.charAt(i) <= 57)
				{
					if(state == 1)
					{
						Expr.Factor[Expr.TermNumber] = Expr.Factor[Expr.TermNumber] *10 + (str.charAt(i) - 48);
					}else if(state == 0)
					{
						Expr.Factor[Expr.TermNumber] = Expr.Factor[Expr.TermNumber] * (str.charAt(i) - 48);
						state = 1;
					}
				}else if(str.charAt(i) >= 97 && str.charAt(i) <= 122)
				{  
					if(Expr.Variable.contains(str.charAt(i)))
					{
						int index = Expr.Variable.indexOf(str.charAt(i));
						//System.out.println(index+"yes");
						Expr.VariableCount[index][Expr.TermNumber] ++;
					}else
					{
						Expr.Variable.add(str.charAt(i));
						int index = Expr.Variable.indexOf(str.charAt(i));
						//System.out.println(index+"first");
						Expr.VariableCount[index][Expr.TermNumber] ++;
						Expr.VariableNumber ++;
					}
					state = 0;
				}else if(str.charAt(i) == 43)
				{
					Expr.TermNumber ++;
					state = 0;
				}else if(str.charAt(i) == 42)
				{
					state = 0;
				}
				//System.out.println(Expr.Factor[Expr.TermNumber]);
			}
			//System.out.println("trmn"+Expr.TermNumber);
			//System.out.println("vrbn"+Expr.VariableNumber);
			
			for(int i = 0; i < Expr.TermNumber + 1; i ++)
			{
				for(int j = 0; j < Expr.VariableNumber; j ++)
				{
					//System.out.println(i+"a"+j);
					//System.out.println(Expr.VariableCount[j][i]);
				}
			}
			
			//showexpr(Expr);
			System.out.println("终于："+System.currentTimeMillis());
			System.out.println("执行耗时 : "+(System.currentTimeMillis()-a)+" 毫秒 ");	
		return Expr;	
	}
	
	static Expression simplify(String str,Expression smpl)
	{
		long t=System.currentTimeMillis();
		System.out.println("始于："+t);
		String regex = "[a-z]=[0-9]+";
		Pattern pat = Pattern.compile(regex);
		Matcher mat = pat.matcher(str);
		while(mat.find())
		{
			//System.out.println(mat.group());
			String val = mat.group();
			if(!smpl.Variable.contains(val.charAt(0)))
			{
				System.out.println("no this variable");
				continue;
			}
				
			int index = smpl.Variable.indexOf(val.charAt(0));
			String a = val.substring(2,val.length());
			int value = Integer.parseInt(a);
			//System.out.println("value="+value);
			for(int i = 0; i < smpl.TermNumber + 1; i ++)
			{
				if(smpl.VariableCount[index][i] != 0)
				{
					int count = smpl.VariableCount[index][i];
					for(int j = 0; j < count; j ++)
					{
						smpl.Factor[i] = smpl.Factor[i] * value;
					}
					smpl.VariableCount[index][i] = 0;
				}
			}
		}
		int flag1 = smpl.TermNumber + 1;
		int flag2[] = new int[10];
		
		for(int i = 0; i < smpl.TermNumber + 1; i ++)
		{
			for(int j = 0; j < smpl.VariableNumber; j ++)
			{
				if(smpl.VariableCount[j][i] != 0)
				{	flag1 --;
					flag2[i] ++;
					break;
				}	
			}
		}
		if(flag1 > 1)
		{
			int sum = 0;
			for(int i = 0; i < smpl.TermNumber + 1; i ++)
			{
				if(flag2[i] == 0)
				{
					sum += smpl.Factor[i];
				}
			}
			Expression rtn = new Expression();
			rtn.TermNumber = smpl.TermNumber - flag1 + 1;
			for(int i = 0; i < rtn.TermNumber; i ++)
			{
				for(int j = 0; i < smpl.TermNumber + 1; i ++)
				{
					if(flag2[j] != 0)
					{
						rtn.Factor[i] = smpl.Factor[j];
						rtn.Variable = smpl.Variable;
						rtn.VariableNumber = smpl.VariableNumber;
						for(int k = 0; k < rtn.VariableNumber; k ++)
							rtn.VariableCount[k][i] = smpl.VariableCount[k][j];
					}
				}
			}
			rtn.Factor[rtn.TermNumber] = sum;
			System.out.println("终于："+System.currentTimeMillis());
			System.out.println("执行耗时 : "+(System.currentTimeMillis()-t)+" 毫秒 ");	
			//showexpr(rtn);
			return rtn;
		}
		//showexpr(smpl);
		System.out.println("终于："+System.currentTimeMillis());
		System.out.println("执行耗时 : "+(System.currentTimeMillis()-t)+" 毫秒 ");	
		return smpl; 
	}  
	
	static Expression derivative(String str,Expression expr)
	{

		long t=System.currentTimeMillis();
		System.out.println("始于："+t);
		char a = str.charAt(5);
		
		if(!expr.Variable.contains(a))
		{
			System.out.println("no this variable");
			return expr;
		}
			
		else
		{
			Expression der = new Expression();
			int count = 0;
			int index = expr.Variable.indexOf(a);
			for(int i = 0; i < expr.TermNumber + 1; i ++)
			{
				if(expr.VariableCount[index][i] != 0)
				{
					der.Factor[count] = expr.Factor[i]*expr.VariableCount[index][i];
					der.VariableCount[index][count] = expr.VariableCount[index][i] - 1;
					for(int j = 0; j < expr.VariableNumber; j ++)
					{
						if(j != index)
							der.VariableCount[j][count] = expr.VariableCount[j][i];
					}
					count ++;
				}
			}
			der.TermNumber = count - 1;
			der.Variable = expr.Variable;
			der.VariableNumber = expr.VariableNumber;
			//showexpr(der);
			System.out.println("终于："+System.currentTimeMillis());
			System.out.println("执行耗时 : "+(System.currentTimeMillis()-t)+" 毫秒 ");
			return der;
		}
		
		
	}
	
	static void showexpr(Expression show)
	{
		for(int i = 0 ; i < show.TermNumber + 1; i ++)
		{
			int n = 0;
			int m = 0;
			if(i != 0)
			{
				System.out.print('+');
				n = 0;
			}
			if(show.Factor[i]!=1)
			{
				System.out.print(show.Factor[i]);
				n = 1;
			}
				
			for(int j = 0; j < show.VariableNumber; j ++)
			{
				for(int k = 0; k < show.VariableCount[j][i]; k ++)
				{
					if(m != 0 || n == 1)
						System.out.print('*');
					System.out.print(show.Variable.get(j));
					m ++;
				}
			}
		}
		System.out.println();
	}

}