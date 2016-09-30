package org.myproject.dto;
/**
 *��¶��ɱ��ַdto��data transport object��
 * 
 */
public class Exposer {
	//�Ƿ�����ɱ
	private boolean exposed;
	//��ɱid
	private long seckillId;
	//ϵͳ��ǰʱ��
	private long now;
	//��ɱ��ʼʱ��
	private long start;
	//��ɱ����ʱ��
	private long end;
	//һ�ּ��ܴ�ʩ
	private String md5;
	public Exposer(boolean exposed, long seckillId, String md5) {
		super();
		this.exposed = exposed;
		this.seckillId = seckillId;
		this.md5 = md5;
	}
	public Exposer(boolean exposed,long seckillId, long now, long start, long end) {
		super();
		this.exposed = exposed;
		this.seckillId = seckillId;
		this.now = now;
		this.start = start;
		this.end = end;
	}
	public Exposer(boolean exposed, long seckillId) {
		super();
		this.exposed = exposed;
		this.seckillId = seckillId;
	}
	public boolean isExposed() {
		return exposed;
	}
	public void setExposed(boolean exposed) {
		this.exposed = exposed;
	}
	public long getSeckillId() {
		return seckillId;
	}
	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}
	public long getNow() {
		return now;
	}
	public void setNow(long now) {
		this.now = now;
	}
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	public long getEnd() {
		return end;
	}
	public void setEnd(long end) {
		this.end = end;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	@Override
	public String toString() {
		return "Exposer [exposed=" + exposed + ", seckillId=" + seckillId + ", now=" + now + ", start=" + start
				+ ", end=" + end + ", md5=" + md5 + "]";
	}
	
}
