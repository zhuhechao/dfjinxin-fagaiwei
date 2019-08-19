
package io.dfjinxin.common.utils.excel;

import java.util.List;

public class ExcelHeadXML
{
    private String title;
    private int uniteRowStart;
    private int uniteRowEnd;
    private int uniteColEnd;
    private int uniteColStart;
    private List<String> head;//t头部列名
    private int headLength;//宽度
    private boolean isSeq;//是否需要序号
    private boolean fit;//单元格是否自动适应宽度
    
    public boolean isFit() {
		return fit;
	}

	public void setFit(boolean fit) {
		this.fit = fit;
	}

	public boolean isSeq() {
		return isSeq;
	}

	public void setSeq(boolean isSeq) {
		this.isSeq = isSeq;
	}

	public List<String> getHead()
    {
        return head;
    }

    public void setHead( List<String> head )
    {
        this.head = head;
    }

    public int getHeadLength()
    {
        return headLength;
    }

    public void setHeadLength( int headLength )
    {
        this.headLength = headLength;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle( String title )
    {
        this.title = title;
    }

    public int getUniteRowStart()
    {
        return uniteRowStart;
    }

    public void setUniteRowStart( int uniteRowStart )
    {
        this.uniteRowStart = uniteRowStart;
    }

    public int getUniteRowEnd()
    {
        return uniteRowEnd;
    }

    public void setUniteRowEnd( int uniteRowEnd )
    {
        this.uniteRowEnd = uniteRowEnd;
    }

    public int getUniteColEnd()
    {
        return uniteColEnd;
    }

    public void setUniteColEnd( int uniteColEnd )
    {
        this.uniteColEnd = uniteColEnd;
    }

    public int getUniteColStart()
    {
        return uniteColStart;
    }

    public void setUniteColStart( int uniteColStart )
    {
        this.uniteColStart = uniteColStart;
    }

}
