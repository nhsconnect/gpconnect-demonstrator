package net.nhs.esb.rest.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name ="queryResponseData")
public class AqlResponse {
	private Meta meta;
	private List<AqlResult> resultSet;
	private String aql;
	private String executedAql;
	
	public Meta getMeta() {
		return meta;
	}
	
	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public List<AqlResult> getResultSet() {
		return resultSet;
	}

	public void setResultSet(List<AqlResult> resultSet) {
		this.resultSet = resultSet;
	}

	public String getAql() {
		return aql;
	}

	public void setAql(String aql) {
		this.aql = aql;
	}

	public String getExecutedAql() {
		return executedAql;
	}

	public void setExecutedAql(String executedAql) {
		this.executedAql = executedAql;
	}
}
