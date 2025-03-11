package com.beauty1nside.purchs;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.List;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.util.StringUtils;


import com.beauty1nside.purchs.dto.ProdUpdateDtlVO;

import oracle.jdbc.OracleConnection;

public class OracleArrayProductUpdateHandler implements TypeHandler<Object> {
	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		return null;
	}
	
	
	@Override
	public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
		return cs.getString(columnIndex);
	}

	//여러 값 보내는 옵션의 컬럼 갯수 지정 
	@Override
	public void setParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
	    if (parameter == null) return;

	   
	  
	    OracleConnection conn = ps.getConnection().unwrap(OracleConnection.class); 	

	    //System.out.println("=====>>>>>"+parameter);
	    List<ProdUpdateDtlVO> files = (List<ProdUpdateDtlVO>) parameter;	   
	    Struct[] array = new Struct[files.size()];
	    
	    int arrayIndex = 0;
	    try {
	    	for ( ProdUpdateDtlVO file : files) {
		    	Object[] filetype = new Object[5];
		    	filetype[0] = new BigDecimal(file.getOptionNum());
		        filetype[1] = file.getOptionName();
		        filetype[2] = new BigDecimal(file.getOptionSafetyInvoice());
		        filetype[3] = new BigDecimal(file.getWarehouseId());
		        filetype[4] = new BigDecimal(file.getOptionUseFlag());
	            array[arrayIndex++] = conn.createStruct("PURCHS_MODIFY_FILE_TYPE", filetype);
		    }
		    Array filearray = conn.createOracleArray("PURCHS_MODIFY_FILE_ARRAY", (Struct[])array);
		    ps.setArray(i, filearray);
	    } catch (SQLException e) {
	    	e.printStackTrace(); // 🔥 오류 로그 출력
	    	throw new SQLException("PURCHS_MODIFY_FILE_TYPE 변환 오류 발생", e);
	    }

	}
	
	


	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {

		String value = "";
		try {
			if (!StringUtils.hasText(rs.getString(columnName))) {
				value = new String(rs.getString(columnName).getBytes("8859_1"), "KSC5601");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;

	}

	

	

}
