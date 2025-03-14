package com.beauty1nside.bhf;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.List;
import oracle.jdbc.OracleConnection;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.util.StringUtils;

import com.beauty1nside.bhf.dto.goodsorder.BhfOrdDtlVO;

public class OracleArrayStructHandler implements TypeHandler<Object> {

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		return null;
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
		return cs.getString(columnIndex);
	}

	@Override
	public void setParameter(PreparedStatement ps, int i, Object parameter,	JdbcType jdbcType) throws SQLException {
		if (parameter == null) return;
		
		OracleConnection conn = ps.getConnection().unwrap(OracleConnection.class); 
		
		List<BhfOrdDtlVO> files = (List<BhfOrdDtlVO>)parameter;
		
	    Object[] filetype = new Object[6]; //필드의 크기가 6, 배열의 크기를 6으로 지정
	    Struct[] array = new Struct[files.size()];

	    int arrayIndex = 0;
	    //배열의 순서는 파일타입 만들떄 컬럼 순서와 맞춰야 한다.
	    for (BhfOrdDtlVO file : files) {
	    	filetype[0] = file.getGoodsCode();
	    	filetype[1] = file.getGoodsName();
	    	filetype[2] = file.getOptionCode();
	    	filetype[3] = file.getOptionName();
	    	filetype[4] = file.getGoodsStandard();
	    	filetype[5] = file.getQuantity();
	    	array[arrayIndex++] = conn.createStruct("FILETYPE", filetype);//FILETYPE은 Oracle에서 지정한 배열타입 이름 
	    }		
		Array filearray = (Array)conn.createOracleArray("FILEARRAY", (Struct[]) array);//FILEARRAY는 Oracle에서 지정한 배열타입 이름		
		ps.setArray(i, filearray);
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
