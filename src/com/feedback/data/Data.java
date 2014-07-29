package com.feedback.data;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.feedback.beans.AbstractItem;
import com.feedback.beans.DataSource;
import com.feedback.beans.DataUnit;

@Entity
@Table(name = "DATA")
public class Data extends AbstractItem {
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private List<DataUnit> dataUnits;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OWNER_DATASOURCE")
	private DataSource dataSource;

	public List<DataUnit> getDataUnits() {
		return this.dataUnits;
	}

	public void addDataUnit(DataUnit dataUnit) {
		this.dataUnits.add(dataUnit);
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}
}
