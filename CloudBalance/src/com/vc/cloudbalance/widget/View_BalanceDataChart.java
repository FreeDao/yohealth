package com.vc.cloudbalance.widget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.vc.cloudbalance.R;
import com.vc.cloudbalance.common.DateHelper;
import com.vc.cloudbalance.model.BalanceDataMDL;
import com.vc.cloudbalance.model.MemberMDL;
import com.vc.cloudbalance.sqlite.BalanceDataDAL;
import com.vc.util.ObjectHelper;

import android.R.integer;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
@EViewGroup(R.layout.view_balancedatachart)
public class View_BalanceDataChart extends  LinearLayout{
	public static final int Week=1;
	public static final int Month=2;
	public static final int Season=3;
	public static final int Year=4;
	public static final int Weight=1;
	public static final int Fat=2;
	public static final int Muscle=3;
	public  static final int Bone=4;
	public static final int Water=5;
	public static final int BMI=6;
	Context mContext;
	@ViewById
	ViewPager viewpager;
	@ViewById
	RelativeLayout llPanel;
	@ViewById
	RadioButton rbDate1,rbDate2,rbDate3,rbDate4,rbType1,rbType2,rbType3,rbType4,rbType5,rbType6;
	@ViewById
	Button btnPre,btnNext;
	MemberMDL member;
	BalanceChartView chartView;
	List<BalanceDataMDL> balanceDatas;
	BalanceDataDAL dal;
	int pageIndex=0;
	public View_BalanceDataChart(Context context,MemberMDL member) {
		super(context);
		mContext=context;
		this.member=member;
		// TODO Auto-generated constructor stub
	}
	public View_BalanceDataChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		// TODO Auto-generated constructor stub
	}
	
	@AfterViews
	void init()
	{
		dal=new BalanceDataDAL(mContext);
		chartView=new BalanceChartView(mContext);
		RelativeLayout.LayoutParams lParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
		chartView.setLayoutParams(lParams);
		llPanel.addView(chartView);
		
		if(TextUtils.isEmpty(member.getMemberid()))
		{
			balanceDatas=new BalanceDataDAL(mContext).SelectByClientMemberId(member.getClientid());
		}
		else {
			balanceDatas=new BalanceDataDAL(mContext).SelectByMemberId(member.getMemberid());
		}
		rbDate1.setOnClickListener(rbClickListener);
		rbDate2.setOnClickListener(rbClickListener);
		rbDate3.setOnClickListener(rbClickListener);
		rbDate4.setOnClickListener(rbClickListener);
		rbType1.setOnClickListener(rbClickListener);
		rbType2.setOnClickListener(rbClickListener);
		rbType3.setOnClickListener(rbClickListener);
		rbType4.setOnClickListener(rbClickListener);
		rbType5.setOnClickListener(rbClickListener);
		rbType6.setOnClickListener(rbClickListener);
		refreshChart();
	}
	@Click(R.id.btnPre)
	void pre()
	{
		pageIndex--;
		refreshChart();
	}
	@Click(R.id.btnNext)
	void next()
	{
		pageIndex++;
		refreshChart();
	}
	OnClickListener rbClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			pageIndex=0;
			if(v.getId()==R.id.rbDate1||v.getId()==R.id.rbDate2||v.getId()==R.id.rbDate3||v.getId()==R.id.rbDate4)
			{
				rbDate1.setChecked(false);
				rbDate2.setChecked(false);
				rbDate3.setChecked(false);
				rbDate4.setChecked(false);
				if(v.getId()==R.id.rbDate1)
				{
					rbDate1.setChecked(true);
				}
				else if(v.getId()==R.id.rbDate2)
				{
					rbDate2.setChecked(true);
				}
				else if(v.getId()==R.id.rbDate3)
				{
					rbDate3.setChecked(true);
				}
				else if(v.getId()==R.id.rbDate4)
				{
					rbDate4.setChecked(true);
				}
				refreshChart();
			}
			else if(v.getId()==R.id.rbType1||v.getId()==R.id.rbType2||v.getId()==R.id.rbType3||v.getId()==R.id.rbType4||v.getId()==R.id.rbType5||v.getId()==R.id.rbType6)
			{
				rbType1.setChecked(false);
				rbType2.setChecked(false);
				rbType3.setChecked(false);
				rbType4.setChecked(false);
				rbType5.setChecked(false);
				rbType6.setChecked(false);
				if(v.getId()==R.id.rbType1)
				{
					rbType1.setChecked(true);
				}
				else if(v.getId()==R.id.rbType2)
				{
					rbType2.setChecked(true);
				}
				else if(v.getId()==R.id.rbType3)
				{
					rbType3.setChecked(true);
				}
				else if(v.getId()==R.id.rbType4)
				{
					rbType4.setChecked(true);
				}
				else if(v.getId()==R.id.rbType5)
				{
					rbType5.setChecked(true);
				}
				else if(v.getId()==R.id.rbType6)
				{
					rbType6.setChecked(true);
				}
				refreshChart();
			}
		}
	};
	
	private void refreshChart()
	{
		int datatype=0;
		if(rbType1.isChecked())
			datatype=Weight;
		else if(rbType2.isChecked())
			datatype=Fat;
		else if(rbType3.isChecked())
			datatype=BMI;
		else if(rbType4.isChecked())
			datatype=Muscle;
		else if(rbType5.isChecked())
			datatype=Bone;
		else if(rbType6.isChecked())
			datatype=Water;
		if(rbDate1.isChecked())
			drawWeek(pageIndex,datatype);
		else if(rbDate2.isChecked())
			drawMonth(pageIndex,datatype);
		else if(rbDate3.isChecked())
			drawSeason(pageIndex,datatype);
		else if(rbDate4.isChecked())
			drawYear(pageIndex,datatype);
	}
	
	private void drawChart(List<Date> nowDate,String[] x_txt,String[] x_top_txt,int[] x_val,int datatype)
	{
		//List<Date> nowWeek=DateHelper.dateToWeek(new Date(), index);
		//String[] x_txt=new String[]{"一","二","三","四","五","六","七"};
		String[] y_txt;
		//int[] x_val=new int[]{1,2,3,4,5,6,7};
		float[] y_val;
		float[] val=null;
		float y_max=0;
		float y_min=0;
		List<Float> val_list=new LinkedList<Float>();
		balanceDatas=dal.SelectDayDataByTime(member.getMemberid(), member.getClientid(), ObjectHelper.Convert2String(nowDate.get(0), "yyyy-MM-dd 00:00:00") , ObjectHelper.Convert2String(nowDate.get(nowDate.size()-1), "yyyy-MM-dd 23:59:59"));
		weekFor: 
		for (int i = 0; i < nowDate.size(); i++) {
			dataFor: 
			for(int ii=0;ii<balanceDatas.size();ii++)
			{
				if(ObjectHelper.Convert2String(nowDate.get(i), "yyyy-MM-dd").equals(ObjectHelper.Convert2String(balanceDatas.get(ii).getWeidate(), "yyyy-MM-dd"))){
					val_list.add(ObjectHelper.Convert2Float(balanceDatas.get(ii).getVal(datatype)));
					continue weekFor;
				}
			}
			val_list.add((float) 0);
		}
		val=new float[val_list.size()];
		for(int i=0;i<val_list.size();i++)
		{
			if(y_max==0&&val_list.get(i)!=0)
			{
				y_max=val_list.get(i);
			}
			if(y_min==0&&val_list.get(i)!=0)
			{
				y_min=val_list.get(i);
			}
			val[i]=val_list.get(i);
			if(y_max<=val_list.get(i))
				y_max=val_list.get(i);
			if(y_min>=val_list.get(i)&&val_list.get(i)!=0)
				y_min=val_list.get(i);
		}
		
		float split=1;
		float cha=y_max-y_min;
		if(cha<=2)
			split=1;
		else if(cha>2&&cha<=7)
			split=5;
		else if(cha>7&&cha<=12)
			split=10;
		else if(cha>12&&cha<=17)
			split=15;
		else if(cha>18&&cha<=23)
			split=20;
		else
			split=20;
		//确定趋势图的Y轴最大点及最小点的值，最大点=数据最大值+增量；最小点=数据最小值-2*增量
		y_max=(float) ((Math.floor(y_max/5)+1)*5)+split;
		y_min=(float) ((Math.floor(y_min/5)-0)*5)-(2*split);
		if(y_min<0)
			y_min=0;
		List<Float> y_list=new LinkedList<Float>();
		int y_count=(int) ((y_max-y_min)/split)+1;
		for(int i=0;i<y_count;i++){
			float v=y_max-(i*split);
			if(v>0)
			{
				y_list.add(0,v);
			}
			else if(v<=0)
			{
				//if(y_list.size()==0)
				y_list.add(0,(float) 0.0);
				break;
			}
		}
		Float[]	y_tmp_val=y_list.toArray(new Float[0]);
		y_val=new float[y_tmp_val.length];
		y_txt=new String[y_tmp_val.length];
		for(int i=0;i<y_tmp_val.length;i++)
		{
			y_val[i]=y_tmp_val[i];
			y_txt[i]=(int)(y_val[i])+"";
		}
		chartView.setXY(x_val, y_val, x_txt,x_top_txt, y_txt,val);
		chartView.invalidate();
	}
	private void drawWeek(int index,int datatype)
	{
		List<Date> nowWeek=DateHelper.dateToWeek(new Date(), index);
		String[] x_txt=new String[]{"一","二","三","四","五","六","七"};
		String[] x_top_txt=new String[]{ObjectHelper.Convert2String(nowWeek.get(0), "yyyy")+";"+ObjectHelper.Convert2String(nowWeek.get(0), "MM/dd"),"","",ObjectHelper.Convert2String(nowWeek.get(3), "yyyy")+";"+ObjectHelper.Convert2String(nowWeek.get(3), "MM/dd"),"","",ObjectHelper.Convert2String(nowWeek.get(6), "yyyy")+";"+ObjectHelper.Convert2String(nowWeek.get(6), "MM/dd")};
		//String[] x_top_txt=new String[]{ObjectHelper.Convert2String(nowWeek.get(0), "yyyy")+";"+ObjectHelper.Convert2String(nowWeek.get(0), "MM/dd"),"","",ObjectHelper.Convert2String(nowWeek.get(3), "yyyy"),"","",ObjectHelper.Convert2String(nowWeek.get(6), "yyyy")};
		//String[] y_txt;
		int[] x_val=new int[]{1,2,3,4,5,6,7};
		drawChart(nowWeek,x_txt,x_top_txt,x_val,datatype);
//		
//		
//		float[] y_val;
//		float[] val=null;
//		float y_max=0;
//		float y_min=0;
//		List<Float> val_list=new LinkedList<Float>();
//		balanceDatas=dal.SelectDayDataByTime(member.getMemberid(), member.getClientid(), ObjectHelper.Convert2String(nowWeek.get(0), "yyyy-MM-dd 00:00:00") , ObjectHelper.Convert2String(nowWeek.get(nowWeek.size()-1), "yyyy-MM-dd 23:59:59"));
//		weekFor: 
//		for (int i = 0; i < nowWeek.size(); i++) {
//			dataFor: 
//			for(int ii=0;ii<balanceDatas.size();ii++)
//			{
//				if(ObjectHelper.Convert2String(nowWeek.get(i), "yyyy-MM-dd").equals(ObjectHelper.Convert2String(balanceDatas.get(ii).getWeidate(), "yyyy-MM-dd"))){
//					val_list.add(ObjectHelper.Convert2Float(balanceDatas.get(ii).getVal(Weight)));
//					continue weekFor;
//				}
//			}
//			val_list.add((float) 0);
//		}
//		val=new float[val_list.size()];
//		for(int i=0;i<val_list.size();i++)
//		{
//			if(y_max==0&&val_list.get(i)!=0)
//			{
//				y_max=val_list.get(i);
//			}
//			if(y_min==0&&val_list.get(i)!=0)
//			{
//				y_min=val_list.get(i);
//			}
//			val[i]=val_list.get(i);
//			if(y_max<=val_list.get(i))
//				y_max=val_list.get(i);
//			if(y_min>=val_list.get(i)&&val_list.get(i)!=0)
//				y_min=val_list.get(i);
//		}
//		
//		float split=1;
//		float cha=y_max-y_min;
//		if(cha<=2)
//			split=1;
//		else if(cha>2&&cha<=7)
//			split=5;
//		else if(cha>7&&cha<=12)
//			split=10;
//		else if(cha>12&&cha<=17)
//			split=15;
//		else if(cha>18&&cha<=23)
//			split=20;
//		else
//			split=20;
//		//确定趋势图的Y轴最大点及最小点的值，最大点=数据最大值+增量；最小点=数据最小值-2*增量
//		y_max=(float) ((Math.floor(y_max/5)+1)*5)+split;
//		y_min=(float) ((Math.floor(y_min/5)-0)*5)-(2*split);
//		if(y_min<0)
//			y_min=0;
//		List<Float> y_list=new LinkedList<Float>();
//		int y_count=(int) ((y_max-y_min)/split)+1;
//		for(int i=0;i<y_count;i++){
//			float v=y_max-(i*split);
//			if(v>0)
//			{
//				y_list.add(0,v);
//			}
//			else if(v<=0)
//			{
//				//if(y_list.size()==0)
//				y_list.add(0,(float) 0.0);
//				break;
//			}
//		}
//		Float[]	y_tmp_val=y_list.toArray(new Float[0]);
//		y_val=new float[y_tmp_val.length];
//		y_txt=new String[y_tmp_val.length];
//		for(int i=0;i<y_tmp_val.length;i++)
//		{
//			y_val[i]=y_tmp_val[i];
//			y_txt[i]=(int)(y_val[i])+"";
//		}
//		chartView.setXY(x_val, y_val, x_txt, y_txt,val);
//		chartView.invalidate();
	}
	private void drawMonth(int index ,int datatype)
	{
		List<Date> nowMonth=DateHelper.dateToMonth(new Date(), index);
		String[] x_txt=new String[]{"1","7","13","19","25",nowMonth.get(nowMonth.size()-1).getDate()+""};
		String[] x_top_txt=new String[]{ObjectHelper.Convert2String(nowMonth.get(0), "yyyy")+";"+ObjectHelper.Convert2String(nowMonth.get(0), "MM/01"),"",ObjectHelper.Convert2String(nowMonth.get(2), "yyyy")+";"+ObjectHelper.Convert2String(nowMonth.get(2), "MM/13"),"",ObjectHelper.Convert2String(nowMonth.get(4), "yyyy")+";"+ObjectHelper.Convert2String(nowMonth.get(4), "MM/25"),""};
		
		int[] x_val=new int[]{1,7,13,19,25,nowMonth.get(nowMonth.size()-1).getDate()};
		drawChart(nowMonth,x_txt,x_top_txt,x_val,datatype);
		
		
	}
	private void drawSeason(int index ,int datatype)
	{
		List<Date> nowSeason=DateHelper.dateToSeason(new Date(), index);
		String[] x_txt;
		String[] x_top_txt=null;
		int[] x_val;
		Calendar cal=Calendar.getInstance();
		
		if(nowSeason.get(0).getMonth()==0){
			x_txt=new String[]{"一月","二月","三月","四月"};
			cal.set(Calendar.MONTH, 0);
			int day1=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			cal.set(Calendar.MONTH,1);
			int day2=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			cal.set(Calendar.MONTH,2);
			int day3=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			x_val = new int[]{1,day1+1,day1+day2+1,day1+day2+day3+1};
			x_top_txt=new String[]{ObjectHelper.Convert2String(nowSeason.get(0), "yyyy")+";"+ObjectHelper.Convert2String(nowSeason.get(0), "01/01"),ObjectHelper.Convert2String(nowSeason.get(1), "yyyy")+";"+ObjectHelper.Convert2String(nowSeason.get(1), "02/01"),ObjectHelper.Convert2String(nowSeason.get(2), "yyyy")+";"+ObjectHelper.Convert2String(nowSeason.get(2), "03/01"),ObjectHelper.Convert2String(nowSeason.get(3), "yyyy")+";"+ObjectHelper.Convert2String(nowSeason.get(3), "04/01")};
			
		}
		else if(nowSeason.get(0).getMonth()==3)
		{
			x_txt=new String[]{"四月","五月","六月","七月"};
			cal.set(Calendar.MONTH, 3);
			int day1=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			cal.set(Calendar.MONTH,4);
			int day2=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			cal.set(Calendar.MONTH,5);
			int day3=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			x_val = new int[]{1,day1+1,day1+day2+1,day1+day2+day3+1};
			x_top_txt=new String[]{ObjectHelper.Convert2String(nowSeason.get(0), "yyyy")+";"+ObjectHelper.Convert2String(nowSeason.get(0), "04/01"),ObjectHelper.Convert2String(nowSeason.get(1), "yyyy")+";"+ObjectHelper.Convert2String(nowSeason.get(1), "05/01"),ObjectHelper.Convert2String(nowSeason.get(2), "yyyy")+";"+ObjectHelper.Convert2String(nowSeason.get(2), "06/01"),ObjectHelper.Convert2String(nowSeason.get(3), "yyyy")+";"+ObjectHelper.Convert2String(nowSeason.get(3), "07/01")};
			
		}
		else if(nowSeason.get(0).getMonth()==6){
			x_txt=new String[]{"七月","八月","九月","十月"};
			cal.set(Calendar.MONTH, 6);
			int day1=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			cal.set(Calendar.MONTH,7);
			int day2=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			cal.set(Calendar.MONTH,8);
			int day3=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			x_val = new int[]{1,day1+1,day1+day2+1,day1+day2+day3+1};
			x_top_txt=new String[]{ObjectHelper.Convert2String(nowSeason.get(0), "yyyy")+";"+ObjectHelper.Convert2String(nowSeason.get(0), "07/01"),ObjectHelper.Convert2String(nowSeason.get(1), "yyyy")+";"+ObjectHelper.Convert2String(nowSeason.get(1), "08/01"),ObjectHelper.Convert2String(nowSeason.get(2), "yyyy")+";"+ObjectHelper.Convert2String(nowSeason.get(2), "09/01"),ObjectHelper.Convert2String(nowSeason.get(3), "yyyy")+";"+ObjectHelper.Convert2String(nowSeason.get(3), "10/01")};
			
		}
		else {
			x_txt=new String[]{"十月","十一月","十二月","一月"};
			cal.set(Calendar.MONTH, 9);
			int day1=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			cal.set(Calendar.MONTH,10);
			int day2=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			cal.set(Calendar.MONTH,11);
			int day3=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			x_val = new int[]{1,day1+1,day1+day2+1,day1+day2+day3+1};
			x_top_txt=new String[]{ObjectHelper.Convert2String(nowSeason.get(0), "yyyy")+";"+ObjectHelper.Convert2String(nowSeason.get(0), "10/01"),ObjectHelper.Convert2String(nowSeason.get(1), "yyyy")+";"+ObjectHelper.Convert2String(nowSeason.get(1), "11/01"),ObjectHelper.Convert2String(nowSeason.get(2), "yyyy")+";"+ObjectHelper.Convert2String(nowSeason.get(2), "12/01"),(ObjectHelper.Convert2Int(ObjectHelper.Convert2String(nowSeason.get(3), "yyyy"))+1)+";"+ObjectHelper.Convert2String(nowSeason.get(3), "01/01")};
			
		}
		
		drawChart(nowSeason,x_txt,x_top_txt,x_val,datatype);
		
	}
	private void drawYear(int index ,int datatype)
	{
		List<Date> nowYear=DateHelper.dateToYear(new Date(), index);
		Calendar calendar  =Calendar.getInstance();
		calendar.setTime(nowYear.get(0));
		String[] x_txt=new String[]{"一月","四月","七月","十月","一月"};
		String[] x_top_txt=new String[]{ObjectHelper.Convert2String(nowYear.get(0), "yyyy")+";"+ObjectHelper.Convert2String(nowYear.get(0), "01/01"),"",ObjectHelper.Convert2String(nowYear.get(2), "yyyy")+";"+ObjectHelper.Convert2String(nowYear.get(2), "07/01"),"",(ObjectHelper.Convert2Int(ObjectHelper.Convert2String(nowYear.get(4), "yyyy"))+1)+";"+ObjectHelper.Convert2String(nowYear.get(4), "01/01")};
		//String[] x_top_txt=new String[]{ObjectHelper.Convert2String(nowMonth.get(0), "yyyy")+";"+ObjectHelper.Convert2String(nowMonth.get(0), "MM/01"),"",ObjectHelper.Convert2String(nowMonth.get(2), "yyyy")+";"+ObjectHelper.Convert2String(nowMonth.get(2), "MM/13"),"",ObjectHelper.Convert2String(nowMonth.get(4), "yyyy")+";"+ObjectHelper.Convert2String(nowMonth.get(4), "MM/25"),""};
		
		int day1=DateHelper.getDayYear(nowYear.get(0).getYear(), 0, 1);
		int day4=DateHelper.getDayYear(nowYear.get(0).getYear(), 3, 1);
		int day7=DateHelper.getDayYear(nowYear.get(0).getYear(), 6, 1);
		int day10=DateHelper.getDayYear(nowYear.get(0).getYear(), 9, 1);
		int day12=calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
		String[] y_txt;
		//int[] x_val=new int[]{day1,day4,day7,day10,day12};
		int[] x_val=new int[]{1,91,182,273,365};
		drawChart(nowYear,x_txt,x_top_txt,x_val,datatype);
		
	}
}
