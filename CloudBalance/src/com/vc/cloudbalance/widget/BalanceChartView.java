package com.vc.cloudbalance.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class BalanceChartView extends View {
	Context mContext;
	private int[] x_val;
	private float[] y_val;
	private String[] x_txt;
	private String[] y_txt;
	private String[] x_top_txt;
	private float[] vals;
	public void setXY(int[] _x_val,float[] _y_val,String[] _x_txt,String[] _x_top_txt,String[] _y_txt,float[] _val)
	{
		x_val=_x_val;
		y_val=_y_val;
		x_txt=_x_txt;
		y_txt=_y_txt;
		x_top_txt=_x_top_txt;
		vals=_val;
	}
	public BalanceChartView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public BalanceChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		// TODO Auto-generated constructor stub
	}

	public BalanceChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		if(x_val==null || y_val==null)
			return ;
		Paint LinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		LinePaint.setColor(Color.WHITE);
		LinePaint.setStrokeWidth(1);
		LinePaint.setStyle(Style.STROKE);

		Paint DottedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		DottedPaint.setAntiAlias(true);
		DottedPaint.setStyle(Paint.Style.STROKE);
		DottedPaint.setColor(Color.GRAY);
		PathEffect effects2 = new DashPathEffect(new float[] { 5, 5, 5, 5 }, 1);
		DottedPaint.setPathEffect(effects2);
		Paint labelPaint = new Paint();
		labelPaint.setAlpha(0x0000ff);
		labelPaint.setTextSize(20);
		labelPaint.setTextAlign(Paint.Align.RIGHT);
		labelPaint.setColor(Color.parseColor("#fff100"));
		Paint smallerlabelPaint = new Paint();
		
		smallerlabelPaint.setTextSize(14);
		smallerlabelPaint.setTextAlign(Paint.Align.CENTER);
		smallerlabelPaint.setColor(Color.WHITE);
		Paint CirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		CirclePaint.setColor(Color.YELLOW);
		CirclePaint.setStrokeWidth(2);
		CirclePaint.setStyle(Style.STROKE);
		Paint CircleStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		CircleStrokePaint.setColor(Color.WHITE);
		CircleStrokePaint.setStrokeWidth(0);
		CircleStrokePaint.setStyle(Style.FILL);
		int width = getWidth();
		int height = getHeight()-10;
//		int[] y = new int[] { 10, 20, 30,40, 50, 60, 70, 80, 90, 100, 110, 120 };
//		int[] x = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
		//float[] vals=new float[]{15,20,30,45,55,65,75,85,95,105,115,125};
		//int max_y=120,min_y=10;
		FontMetrics fontMetrics = labelPaint.getFontMetrics();
		float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
		int y_lable_width = 40;
		float x_lable_width = 40;
		int max_y=(int)y_val[y_val.length-1];
		int min_y=(int)y_val[0];
		int max_x=(int)x_val[x_val.length-1];
		int min_x=(int)x_val[0];
		int padding_top=(int) (x_lable_width*1.5);//预留15，折线点不会去到顶
		int padding_right=0;//预留15，折线点不会去到顶
		float chart_height=height-x_lable_width;
		float chart_width=width;
		float chart_val_line_height=chart_height-padding_top;
		float chart_val_line_width=chart_width;
		float x_offet=0;
		float y_offet=padding_top;
		float yy_padding_top=x_lable_width/2;
		// y轴线
		 //float y_item=(float)chart_line_height*y[y.length-1]/(max_y-min_y);
			
//			   Path path = new Path();
//				path.moveTo(y_lable_width, (float)height - (float)chart_line_height*y[y.length-1]/(max_y-min_y));
//				path.lineTo(width , (float)height - (float)chart_line_height*y[y.length-1]/(max_y-min_y));
//				canvas.drawPath(path, DottedPaint);
//			
		//canvas.drawLine(y_lable_width,0, width, 0 , LinePaint);
		// y轴线
		canvas.drawLine(y_lable_width, 0, y_lable_width, chart_val_line_height+y_offet , LinePaint);
		// x轴线
		canvas.drawLine(y_lable_width, chart_val_line_height+y_offet, chart_width,  chart_val_line_height+y_offet , LinePaint);
		//y轴刻度
		for(int i=0;i<y_val.length;i++)
		{
			 float y_item=chart_val_line_height*(y_val.length-i)/y_val.length +y_offet;
			 y_item=(float)chart_val_line_height*(max_y-y_val[i])/(max_y-min_y)+y_offet;
			if(i!=0){
			   Path path = new Path();
				path.moveTo(y_lable_width, y_item);
				path.lineTo(width ,y_item);
				canvas.drawPath(path, DottedPaint);
			}
			canvas.drawText( y_txt[i],y_lable_width-5, y_item, labelPaint);
			
		}
		//x轴刻度
		labelPaint.setColor(Color.WHITE);
		x_offet=50;
		//chart_val_line_width=(int) (chart_val_line_width-x_offet*3);
		for(int i=0;i<x_val.length;i++)
		{
			labelPaint.setTextAlign(Paint.Align.CENTER);
			
			float x_unit_widtd=(float) ((float)(chart_val_line_width-y_lable_width)/(max_x-min_x+1));
			float x_space_width=(float) ((float)(chart_val_line_width-y_lable_width)/(x_val.length+1));
			float x_item=(float) ((float)(chart_val_line_width-y_lable_width-x_offet*2)*(x_val[i]-1)/(max_x-min_x));
			//x_item=(float)chart_val_line_width*i/(x_val.length);
			
			
			   Path path = new Path();
				path.moveTo(y_lable_width+x_item+x_offet,chart_val_line_height+y_offet);
				path.lineTo(y_lable_width+x_item +x_offet, 0+y_offet/2);
				canvas.drawPath(path, DottedPaint);
			
			canvas.drawText( x_txt[i],x_item+y_lable_width+x_offet, height-5, labelPaint);
			if(!TextUtils.isEmpty(x_top_txt[i]))
			{
//				TextPaint textPaint = new TextPaint();  
//				textPaint.setAntiAlias(true);
//				textPaint.setColor(Color.WHITE);
//				textPaint.setTextSize(20);
//				StaticLayout layout = new StaticLayout(x_top_txt[i], textPaint, 300,Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);  
//				canvas.save();
//				canvas.translate(x_item+y_lable_width+x_offet-10, 0);//
//				layout.draw(canvas);  
//				canvas.restore();//别忘了restore  
				String[] dateStrings =x_top_txt[i].split(";");
				canvas.drawText( dateStrings[0],x_item+y_lable_width+x_offet, 20, labelPaint);
				canvas.drawText( dateStrings[1],x_item+y_lable_width+x_offet,40,  smallerlabelPaint);
			}
			
		}
		//canvas.drawText( "l",chart_val_line_width, height-5, labelPaint);
		Path polygonPath = new Path();
		Path polygonLinePath = new Path();
		Paint polygonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		polygonPaint.setStrokeWidth(0);
		polygonPaint.setStyle(Style.FILL);
		int parsedColor = Color.parseColor("#7Ffcf542");
		polygonPaint.setColor(parsedColor);
		int newPathFlag=0;
		//画圆心点
		for (int i = 0; i < vals.length; i++) {
			if(i==27)
			{
				int aa=2;
			}
			if(vals[i]<=0){
				if(i!=0&&vals[i-1]>0){
					polygonPath.lineTo((float) ((float)(chart_val_line_width-y_lable_width-x_offet*2)*(i-1)/(max_x-min_x))+y_lable_width+x_offet,  chart_val_line_height+y_offet);
					Log.e("vals------", i+"   "+vals[i]+"    "+(float) ((float)(chart_val_line_width-y_lable_width-x_offet*2)*(i-1)/(max_x-min_x)));
					Log.e("polygonPath-3", i+"   "+vals[i]+"    "+(float) ((float)(chart_val_line_width-y_lable_width-x_offet*2)*(i-1)/(max_x-min_x))+"        "+(chart_val_line_height+y_offet));
					polygonPath.close();
					canvas.drawPath(polygonPath,polygonPaint);
					canvas.drawPath(polygonLinePath,CirclePaint);
					polygonPath=new Path();
					polygonLinePath=new Path();
				}
				newPathFlag=0;
				continue;
			}
			float y_item=(float)chart_val_line_height*(max_y-vals[i])/(max_y-min_y)+y_offet;;
			float x_item=(float) ((float)(chart_val_line_width-y_lable_width-x_offet*2)*(i)/(max_x-min_x));
			//float x_item=(float) ((float)(chart_val_line_width-y_lable_width-x_offet*2)*(x_val[i]-1)/(max_x-min_x));
			//x_offet=(float)chart_val_line_width/x_val.length/4*3;
			Log.e("vals", i+"   "+vals[i]+"    "+x_item);
			if(newPathFlag==0)
			{
				polygonPath.moveTo(x_item+y_lable_width+x_offet, chart_val_line_height+y_offet);
				Log.e("polygonPath-1", i+"   "+vals[i]+"    "+(x_item+y_lable_width+x_offet)+"    "+(chart_val_line_height+y_offet));
				polygonLinePath.moveTo(x_item+y_lable_width+x_offet, y_item);
				
			}
			else {
				polygonLinePath.lineTo(x_item+y_lable_width+x_offet, y_item);
			}
			
			
			polygonPath.lineTo(x_item+y_lable_width+x_offet, y_item);
			Log.e("polygonPath-2", i+"   "+vals[i]+"    "+(x_item+y_lable_width+x_offet)+"    "+y_item);
			newPathFlag++;
		}
		//polygonPath.setFillType(FillType.INVERSE_EVEN_ODD);
		
		polygonPath=new Path();
		//polygonPath.moveTo(83.3333364050, y)
		for (int i = 0; i < vals.length; i++) {
			if(vals[i]<=0)
				continue;
			float y_item=(float)chart_val_line_height*(max_y-vals[i])/(max_y-min_y)+y_offet;
			float x_item=(float) ((float)(chart_val_line_width-y_lable_width-x_offet*2)*(i)/(max_x-min_x));
			//x_offet=(float)chart_val_line_width/x_val.length/4*3;
			
			canvas.drawCircle(x_item+y_lable_width+x_offet,y_item, (10 / 2)-1, CircleStrokePaint);
			canvas.drawCircle(x_item+y_lable_width+x_offet,y_item, (10 / 2)-1, CirclePaint);
			if(i!=vals.length-1)
			{
				
			}
		}
		super.onDraw(canvas);
		// canvas.drawColor(Color.GRAY);

	}
}