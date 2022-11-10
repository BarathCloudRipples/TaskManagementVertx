package io.github.task.vertx.api.entity;

import java.io.Serializable;

import javax.persistence.*;

import io.vertx.core.json.JsonObject;

@Entity
@Table(name = "task_details")

public class Task implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "task_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_gen")
    @SequenceGenerator(name = "article_gen", allocationSize = 1, sequenceName = "task_details_task_id_seq")
    private int taskid;
	
	@Column(name = "title", nullable=false)
    private String title;
    
    @Column(name = "description", nullable=false)
    private String description;
    
    @Column(name = "status", nullable=false)
    private String status;
	
    @Column(name = "timeline", nullable=false)
    private String timeline;
    
    @Column(name = "assign_to", nullable=false)
    private String assignto;
  
	
	public int getTaskid() {
		return taskid;
	}


	public void setTaskid(int taskid) {
		this.taskid = taskid;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getTimeline() {
		return timeline;
	}


	public void setTimeline(String timeline) {
		this.timeline = timeline;
	}


	public String getAssignto() {
		return assignto;
	}


	public void setAssignto(String assignto) {
		this.assignto = assignto;
	}


	public String toJsonString(){
        return String.valueOf(JsonObject.mapFrom(this));
   }
    
}
