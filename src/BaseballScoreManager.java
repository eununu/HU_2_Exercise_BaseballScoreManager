// 한양대학교 컴퓨터공학과 2013043360 이지은
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.lang.Integer;

public class BaseballScoreManager {

	ArrayList<Score> u = new ArrayList<Score>(); //경기결과 객체
	Scanner s= new Scanner(System.in);
	
	String teamName[]= {"삼성","두산","롯데","SK","한화","넥센","LG","KIA","NC","KT"}; //팀이름
	int teamMatch[]= new int[10]; //팀별 경기수
	int teamWin[]= new int[10]; //팀별 승리경기수
	int teamLose[]= new int[10]; //팀별 패배경기수
	int teamTie[]= new int[10]; //팀별 무승부경기수
	int teamWinHome[]= new int[10]; //홈팀에서의 팀별 승리경기수
	int teamLoseHome[]= new int[10]; //홈팀에서의 팀별 패배경기수
	int teamTieHome[]= new int[10]; //홈팀에서의 팀별 무승부경기수
	double teamRate[]= new double[10]; //팀별 승률
	int rank[]= new int[10]; //랭킹
	int resultWin[][]= new int[10][10]; // 상대전적 
	int resultLose[][]= new int[10][10];  
	int resultTie[][]= new int[10][10]; 
	
	int count= 1;
	
	void addDataFromFile(String line)
	{
		int hs,as,home=0,away=0;
		String list[]= line.split(" "); // (2015/03/07) (두산) (9) (:) (4) (삼성)
		
		hs= Integer.parseInt(list[2]);
		as= Integer.parseInt(list[4]);
		
		for(int i=0;i<10;i++)
		{
			if(list[1].equals(teamName[i])) home= i;
			if(list[5].equals(teamName[i])) away= i;
		}
		
		//list[0]= date, list[1]= 홈팀, list[5]= 어웨이팀
		u.add(new Score(count++,list[0],list[1],list[5],home,away,hs,as));	
	
		teamMatch[home]++;
		teamMatch[away]++;
		
		recordMatch(home,away,hs,as,1);
	}
	
	void start() //관리작업 진행
	{
		int menu,t,tnum;
		String tname;
		
		while(true)
		{
			System.out.println("================================================================");
			System.out.println("1)경기결과 등록 2)경기결과 수정 3)경기결과 출력 4)경기결과 검색");
			System.out.print("5)경기통계 6)상대전적 7)종료 => ");
			menu=s.nextInt();
			System.out.println("================================================================");
			
			if(menu==7) 
			{
				System.out.println("=> 종료되었습니다.");
				break;
			}
			else if(menu==1) addScore();
			else if(menu==2) modifyScore();
			else if(menu==3) listScores();
			else if(menu==4)
			{
				System.out.println("Team Number로 검색하시려면 1번,");
				System.out.print("Team Name으로 검색하시려면 2번을 입력해 주세요 : ");
				t= s.nextInt();
				if(t==1) 
				{
					while(true)
					{
						System.out.print("Team Number : ");
						tnum= s.nextInt();
						if(tnum<0 || tnum>9) System.out.println("Team Number가 잘못되었습니다. 다시 입력해주세요.");
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
						if(tnum==-1) System.out.println("Team Name이 잘못되었습니다. 다시 입력해주세요.");
						else
						{
							findScores(tname);
							break;
						}
					}					
				}
				System.out.printf("%d승 %d무 %d패, 승률 %.3f%\n",teamWin[tnum],teamTie[tnum],teamLose[tnum],teamRate[tnum]);
		
			}
			else if(menu==5) viewResult();
			else viewMatchScore();
			System.out.println();
		}
	}
	
	void recordMatch(int h, int a, int hs, int as, int c) //추가 : 각 경기당 승점계산 및 경기수 기록, 수정할때 이전의 경기기록 삭제에도 사용
	{
		if(hs> as) //홈팀이 어웨이팀을 이긴경우
		{
			teamWin[h]+=1*c;
			teamLose[a]+=1*c;
			teamWinHome[h]+=1*c;
			resultWin[h][a]+=1*c;
			resultLose[a][h]+=1*c;
		}
		
		else if(hs< as) //어웨이팀이 홈팀을 이긴경우
		{
			teamWin[a]+=1*c;
			teamLose[h]+=1*c;
			teamLoseHome[h]+=1*c;
			resultWin[a][h]+=1*c;
			resultLose[h][a]+=1*c;
		}
		
		else //비긴경우
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
	
	void addScore() //경기결과등록
	{
		String matchDate;
		int home,away;
		int homeScore,awayScore;
	
		s.nextLine();
		for(int i=0;i<10;i++)
		{
			System.out.println("Team Number "+i+". "+teamName[i]);
		}
		
		System.out.printf("\n경기일자(Match Date) : ");
		matchDate= s.nextLine();
		
		while(true)
		{
			System.out.print("참여 팀(Team Number) : ");
			home= s.nextInt();
			away= s.nextInt();
			if(home<0 || home>9 || away<0 || away>9) System.out.println("Team Number가 잘못되었습니다. 다시 입력해주세요.");
			else if(home==away) System.out.println("같은 팀 끼리는 경기를 하지 않습니다. 다시 입력해주세요.");
			else break;
		}
		
		System.out.print("점수결과(MatchScore) : ");
		homeScore= s.nextInt();
		awayScore= s.nextInt();
		
		u.add(new Score(count++,matchDate,teamName[home],teamName[away],home,away,homeScore,awayScore));
		
		teamMatch[home]++;
		teamMatch[away]++;
		
		recordMatch(home,away,homeScore,awayScore,1);
		System.out.println("=> 등록되었습니다.");
	}
	
	void modifyScore() //경기결과수정
	{
		int n,hs,as;
		System.out.println();
		while(true)
		{
			System.out.print("수정할 경기번호를 입력해 주세요. ");
			n= s.nextInt();
			if(n<0 || n>u.size()) System.out.println("해당하는 경기번호가 없습니다. 다시 입력해 주세요. ");
			else break;
		}
		
		recordMatch(u.get(n-1).home,u.get(n-1).away,u.get(n-1).homeScore,u.get(n-1).awayScore,-1); //이전결과 기록삭제
		
		System.out.print("점수결과(MatchScore) : ");
		hs= s.nextInt();
		as= s.nextInt();
		
		u.get(n-1).setScore(hs,as);
		
		recordMatch(u.get(n-1).home,u.get(n-1).away,hs,as,1);
		System.out.println("=> 수정되었습니다.");
		System.out.println();
		listScores();
	}
	
	void listScores() //등록된 모든 경기결과출력
	{
		System.out.printf("%-5s %-15s %-15s %-11s %-15s\n","No.","Match Date","Home team","Score","Away team\n");
		for(int i=0;i<u.size();i++)
		{
			u.get(i).print();
		}
	}
	
	void findScores(int no) //경기결과검색 - 팀번호
	{
		System.out.println();
		System.out.printf("%-5s %-15s %-15s %-11s %-15s\n","No.","Match Date","Home team","Score","Away team\n");
		for(int i=0;i<u.size();i++)
		{
			if(u.get(i).home==no || u.get(i).away==no) u.get(i).print();
		}
	}
	
	void findScores(String name) //경기결과검색 - 팀이름
	{
		System.out.println();
		System.out.printf("%-5s %-15s %-15s %-11s %-15s\n","No.","Match Date","Home team","Score","Away team\n");
		for(int i=0;i<u.size();i++)
		{
			if(u.get(i).homeTeamName.equals(name) || u.get(i).awayTeamName.equals(name)) u.get(i).print();
		}
	}
	
	void viewResult() //경기통계
	{
		int maxTeam= 0;
		double max= -1.0;
		for(int i=0;i<10;i++)
		{
			System.out.printf("%-15s : %d승 %d무 %d패, 승률 %.2f%\n",teamName[i],teamWin[i],teamTie[i],teamLose[i],teamRate[i]);
			if(max< teamRate[i])
			{
				max= teamRate[i];
				maxTeam= i;
			}
		}
		System.out.println();
		System.out.printf("승률이 가장 높은 팀은 %s 입니다.\n",teamName[maxTeam]);
	}
	
	void viewMatchScore() //상대전적
	{
		int t1,t2;
		while(true)
		{
			System.out.println("상대전적을 비교하고 싶은 팀 2개를 입력해 주세요(Team Number) : ");
			t1= s.nextInt();
			t2= s.nextInt();
			if(t1<0 || t1>9 || t2<0 || t2>9) System.out.println("Team Number가 잘못되었습니다. 다시 입력해주세요.");
			else if(t1==t2) System.out.println("같은 팀 끼리는 경기를 하지 않습니다. 다시 입력해주세요.");
			else break;	
		}
		
		System.out.printf("%s의 %s 상대전적 - %d승 %d무 %d패\n",teamName[t1],teamName[t2],resultWin[t1][t2],resultTie[t1][t2],resultLose[t1][t2]);
		System.out.printf("%s의 %s 상대전적 - %d승 %d무 %d패\n",teamName[t2],teamName[t1],resultWin[t2][t1],resultTie[t2][t1],resultLose[t2][t1]);
	}
	
	void findRank() //랭킹구하기
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
		pw.printf("%-5s%-15s%-5s%-5s%-5s%-5s%-11s%-11s\r\n","순위","팀명","승","패","무","승률","홈경기성적","원정경기성적");
		
		int t;
		
		for(int i=0;i<10;i++)
		{
			t= rank[i];
			pw.printf("%-5d%-15s%-5d%-5d%-5d%-3.3f %-3d-%-3d-%-3d%-3d-%-3d-%-3d\r\n",(i+1),teamName[t],teamWin[t],teamLose[t],teamTie[t],teamRate[t],
					teamWinHome[t],teamTieHome[t],teamLoseHome[t],
					teamWin[t]-teamWinHome[t],teamTie[t]-teamTieHome[t],teamLose[t]-teamLoseHome[t]);
				
		}
		
		pw.printf("\r\n%-5s","팀명");
		for(int i=0;i<10;i++)
		{
			pw.printf("%-13s",teamName[i]);
		}
		pw.printf("%-13s\r\n","합계");
		
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
