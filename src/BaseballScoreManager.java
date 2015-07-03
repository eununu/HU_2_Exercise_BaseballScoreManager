// �Ѿ���б� ��ǻ�Ͱ��а� 2013043360 ������
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.lang.Integer;

public class BaseballScoreManager {

	ArrayList<Score> u = new ArrayList<Score>(); //����� ��ü
	Scanner s= new Scanner(System.in);
	
	String teamName[]= {"�Ｚ","�λ�","�Ե�","SK","��ȭ","�ؼ�","LG","KIA","NC","KT"}; //���̸�
	int teamMatch[]= new int[10]; //���� ����
	int teamWin[]= new int[10]; //���� �¸�����
	int teamLose[]= new int[10]; //���� �й����
	int teamTie[]= new int[10]; //���� ���ºΰ���
	int teamWinHome[]= new int[10]; //Ȩ�������� ���� �¸�����
	int teamLoseHome[]= new int[10]; //Ȩ�������� ���� �й����
	int teamTieHome[]= new int[10]; //Ȩ�������� ���� ���ºΰ���
	double teamRate[]= new double[10]; //���� �·�
	int rank[]= new int[10]; //��ŷ
	int resultWin[][]= new int[10][10]; // ������� 
	int resultLose[][]= new int[10][10];  
	int resultTie[][]= new int[10][10]; 
	
	int count= 1;
	
	void addDataFromFile(String line)
	{
		int hs,as,home=0,away=0;
		String list[]= line.split(" "); // (2015/03/07) (�λ�) (9) (:) (4) (�Ｚ)
		
		hs= Integer.parseInt(list[2]);
		as= Integer.parseInt(list[4]);
		
		for(int i=0;i<10;i++)
		{
			if(list[1].equals(teamName[i])) home= i;
			if(list[5].equals(teamName[i])) away= i;
		}
		
		//list[0]= date, list[1]= Ȩ��, list[5]= �������
		u.add(new Score(count++,list[0],list[1],list[5],home,away,hs,as));	
	
		teamMatch[home]++;
		teamMatch[away]++;
		
		recordMatch(home,away,hs,as,1);
	}
	
	void start() //�����۾� ����
	{
		int menu,t,tnum;
		String tname;
		
		while(true)
		{
			System.out.println("================================================================");
			System.out.println("1)����� ��� 2)����� ���� 3)����� ��� 4)����� �˻�");
			System.out.print("5)������ 6)������� 7)���� => ");
			menu=s.nextInt();
			System.out.println("================================================================");
			
			if(menu==7) 
			{
				System.out.println("=> ����Ǿ����ϴ�.");
				break;
			}
			else if(menu==1) addScore();
			else if(menu==2) modifyScore();
			else if(menu==3) listScores();
			else if(menu==4)
			{
				System.out.println("Team Number�� �˻��Ͻ÷��� 1��,");
				System.out.print("Team Name���� �˻��Ͻ÷��� 2���� �Է��� �ּ��� : ");
				t= s.nextInt();
				if(t==1) 
				{
					while(true)
					{
						System.out.print("Team Number : ");
						tnum= s.nextInt();
						if(tnum<0 || tnum>9) System.out.println("Team Number�� �߸��Ǿ����ϴ�. �ٽ� �Է����ּ���.");
						else 
						{
							findScores(tnum);
							break;
						}
					}
				}
				else
				{
					s.nextLine();
					while(true)
					{
						System.out.print("Team Name : ");
						tname= s.nextLine();
						tnum= -1;
						for(int i=0;i<10;i++)
						{
							if(tname.equals(teamName[i]))
							{
								tnum= i;
								break;
							}
						}
						if(tnum==-1) System.out.println("Team Name�� �߸��Ǿ����ϴ�. �ٽ� �Է����ּ���.");
						else
						{
							findScores(tname);
							break;
						}
					}					
				}
				System.out.printf("%d�� %d�� %d��, �·� %.3f%\n",teamWin[tnum],teamTie[tnum],teamLose[tnum],teamRate[tnum]);
		
			}
			else if(menu==5) viewResult();
			else viewMatchScore();
			System.out.println();
		}
	}
	
	void recordMatch(int h, int a, int hs, int as, int c) //�߰� : �� ���� ������� �� ���� ���, �����Ҷ� ������ ����� �������� ���
	{
		if(hs> as) //Ȩ���� ��������� �̱���
		{
			teamWin[h]+=1*c;
			teamLose[a]+=1*c;
			teamWinHome[h]+=1*c;
			resultWin[h][a]+=1*c;
			resultLose[a][h]+=1*c;
		}
		
		else if(hs< as) //��������� Ȩ���� �̱���
		{
			teamWin[a]+=1*c;
			teamLose[h]+=1*c;
			teamLoseHome[h]+=1*c;
			resultWin[a][h]+=1*c;
			resultLose[h][a]+=1*c;
		}
		
		else //�����
		{
			teamTie[h]+=1*c;
			teamTie[a]+=1*c;
			teamTieHome[h]+=1*c;
			resultTie[h][a]+=1*c;
			resultTie[a][h]+=1*c;
		}
		
		teamRate[h]= teamWin[h]/(teamMatch[h]*1.0);
		teamRate[a]= teamWin[a]/(teamMatch[a]*1.0);
	}
	
	void addScore() //��������
	{
		String matchDate;
		int home,away;
		int homeScore,awayScore;
	
		s.nextLine();
		for(int i=0;i<10;i++)
		{
			System.out.println("Team Number "+i+". "+teamName[i]);
		}
		
		System.out.printf("\n�������(Match Date) : ");
		matchDate= s.nextLine();
		
		while(true)
		{
			System.out.print("���� ��(Team Number) : ");
			home= s.nextInt();
			away= s.nextInt();
			if(home<0 || home>9 || away<0 || away>9) System.out.println("Team Number�� �߸��Ǿ����ϴ�. �ٽ� �Է����ּ���.");
			else if(home==away) System.out.println("���� �� ������ ��⸦ ���� �ʽ��ϴ�. �ٽ� �Է����ּ���.");
			else break;
		}
		
		System.out.print("�������(MatchScore) : ");
		homeScore= s.nextInt();
		awayScore= s.nextInt();
		
		u.add(new Score(count++,matchDate,teamName[home],teamName[away],home,away,homeScore,awayScore));
		
		teamMatch[home]++;
		teamMatch[away]++;
		
		recordMatch(home,away,homeScore,awayScore,1);
		System.out.println("=> ��ϵǾ����ϴ�.");
	}
	
	void modifyScore() //���������
	{
		int n,hs,as;
		System.out.println();
		while(true)
		{
			System.out.print("������ ����ȣ�� �Է��� �ּ���. ");
			n= s.nextInt();
			if(n<0 || n>u.size()) System.out.println("�ش��ϴ� ����ȣ�� �����ϴ�. �ٽ� �Է��� �ּ���. ");
			else break;
		}
		
		recordMatch(u.get(n-1).home,u.get(n-1).away,u.get(n-1).homeScore,u.get(n-1).awayScore,-1); //������� ��ϻ���
		
		System.out.print("�������(MatchScore) : ");
		hs= s.nextInt();
		as= s.nextInt();
		
		u.get(n-1).setScore(hs,as);
		
		recordMatch(u.get(n-1).home,u.get(n-1).away,hs,as,1);
		System.out.println("=> �����Ǿ����ϴ�.");
		System.out.println();
		listScores();
	}
	
	void listScores() //��ϵ� ��� ��������
	{
		System.out.printf("%-5s %-15s %-15s %-11s %-15s\n","No.","Match Date","Home team","Score","Away team\n");
		for(int i=0;i<u.size();i++)
		{
			u.get(i).print();
		}
	}
	
	void findScores(int no) //������˻� - ����ȣ
	{
		System.out.println();
		System.out.printf("%-5s %-15s %-15s %-11s %-15s\n","No.","Match Date","Home team","Score","Away team\n");
		for(int i=0;i<u.size();i++)
		{
			if(u.get(i).home==no || u.get(i).away==no) u.get(i).print();
		}
	}
	
	void findScores(String name) //������˻� - ���̸�
	{
		System.out.println();
		System.out.printf("%-5s %-15s %-15s %-11s %-15s\n","No.","Match Date","Home team","Score","Away team\n");
		for(int i=0;i<u.size();i++)
		{
			if(u.get(i).homeTeamName.equals(name) || u.get(i).awayTeamName.equals(name)) u.get(i).print();
		}
	}
	
	void viewResult() //������
	{
		int maxTeam= 0;
		double max= -1.0;
		for(int i=0;i<10;i++)
		{
			System.out.printf("%-15s : %d�� %d�� %d��, �·� %.2f%\n",teamName[i],teamWin[i],teamTie[i],teamLose[i],teamRate[i]);
			if(max< teamRate[i])
			{
				max= teamRate[i];
				maxTeam= i;
			}
		}
		System.out.println();
		System.out.printf("�·��� ���� ���� ���� %s �Դϴ�.\n",teamName[maxTeam]);
	}
	
	void viewMatchScore() //�������
	{
		int t1,t2;
		while(true)
		{
			System.out.println("��������� ���ϰ� ���� �� 2���� �Է��� �ּ���(Team Number) : ");
			t1= s.nextInt();
			t2= s.nextInt();
			if(t1<0 || t1>9 || t2<0 || t2>9) System.out.println("Team Number�� �߸��Ǿ����ϴ�. �ٽ� �Է����ּ���.");
			else if(t1==t2) System.out.println("���� �� ������ ��⸦ ���� �ʽ��ϴ�. �ٽ� �Է����ּ���.");
			else break;	
		}
		
		System.out.printf("%s�� %s ������� - %d�� %d�� %d��\n",teamName[t1],teamName[t2],resultWin[t1][t2],resultTie[t1][t2],resultLose[t1][t2]);
		System.out.printf("%s�� %s ������� - %d�� %d�� %d��\n",teamName[t2],teamName[t1],resultWin[t2][t1],resultTie[t2][t1],resultLose[t2][t1]);
	}
	
	void findRank() //��ŷ���ϱ�
	{
		int check[]= new int[10];
		double max= -1.0;
		int t=0;
		for(int j=0;j<10;j++)
		{
			for(int i=0;i<10;i++)
			{
				if(check[i]==1) continue;
				if(max< teamRate[i])
				{
					max= teamRate[i];
					t= i;
				}
			}
			max= -1.0;
			check[t]= 1;
			rank[j]= t;
		}
	}
	
	void fileOutput()
	{
		PrintWriter pw;
		try {
			pw = new PrintWriter(new FileWriter("report.txt"));
		
		findRank();
		pw.printf("%-5s%-15s%-5s%-5s%-5s%-5s%-11s%-11s\r\n","����","����","��","��","��","�·�","Ȩ��⼺��","������⼺��");
		
		int t;
		
		for(int i=0;i<10;i++)
		{
			t= rank[i];
			pw.printf("%-5d%-15s%-5d%-5d%-5d%-3.3f %-3d-%-3d-%-3d%-3d-%-3d-%-3d\r\n",(i+1),teamName[t],teamWin[t],teamLose[t],teamTie[t],teamRate[t],
					teamWinHome[t],teamTieHome[t],teamLoseHome[t],
					teamWin[t]-teamWinHome[t],teamTie[t]-teamTieHome[t],teamLose[t]-teamLoseHome[t]);
				
		}
		
		pw.printf("\r\n%-5s","����");
		for(int i=0;i<10;i++)
		{
			pw.printf("%-13s",teamName[i]);
		}
		pw.printf("%-13s\r\n","�հ�");
		
		for(int i=0;i<10;i++)
		{
			pw.printf("%-5s",teamName[i]);
			for(int j=0;j<10;j++)
			{
				pw.printf(" %-3d-%-3d-%-3d   ",resultWin[i][j],resultTie[i][j],resultLose[i][j]);
			}
			pw.printf(" %-3d-%-3d-%-3d   \r\n",teamWin[i],teamLose[i],teamTie[i]);
		}
		pw.close();
	}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static void main(String[] args) throws IOException
	{
		// TODO Auto-generated method stub
		BaseballScoreManager bsm= new BaseballScoreManager();
		String line;
		BufferedReader br= new BufferedReader(new FileReader("scoredata.txt"));
		line= br.readLine();
		while(line!= null)
		{
			bsm.addDataFromFile(line);
			line= br.readLine();
		}
		br.close();
		bsm.start();
		bsm.fileOutput();
	}
}
