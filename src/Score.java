
public class Score {
	
	String date, homeTeamName, awayTeamName; // TeamName 추가
	/*home: 홈팀번호, away: 원정팀번호,
	 homeScore: 홈팀점수, awayScore: 원정팀점수,
	 homeTeamName: 홈팀이름, awayTeamName: 원정팀이름,
	 no: 경기번호, date: 경기일자
	 */
	 
	int home,away; 
	int homeScore,awayScore;
	int no;
	
	Score(int c, String da, String homeName, String awayName, int ho, int aw, int hs, int as)
	{
		this.no= c;
		this.date= da;
		this.homeTeamName= homeName;
		this.awayTeamName= awayName;
		this.home= ho;
		this.away= aw;
		this.homeScore= hs;
		this.awayScore= as;
	}
	
	void print()
	{
		System.out.printf("%-5d %-15s %-15s %-3d:  %-5d %-15s\n",no,date,homeTeamName,homeScore,awayScore,awayTeamName);
	}
		
	void setScore(int mohoScore, int moawScore) //Score 수정
	{
		this.homeScore= mohoScore;
		this.awayScore= moawScore;
	}
}
