SELECT  B.STOCK_CODE, A.STOCK_STATUS, A.STOCK_SECTIONX1 AS CRITICIDAD,
            A.ITEM_NAME, B.INVENT_COST_PR,  B.CLASS, B.STOCK_TYPE, B.UNIT_OF_ISSUE,
            (A.DESC_LINEX1  || ' ' || A.DESC_LINEX2  || ' ' ||  A.DESC_LINEX3  || ' ' || A.DESC_LINEX4) as DS,   
            (SELECT b1.CAT_EXT_DATA
            FROM ellrep.MSF100 a1 INNER JOIN ellrep.MSF1CC b1 ON a1.CAT_EXT_UUID = b1.CAT_EXT_UUID
            and a1.STOCK_CODE=B.STOCK_CODE
            ) as DS_LARGE,
            B.DUES_IN, B.IN_TRANSIT, B.CONSIGN_ITRANS, B.TOTAL_PICKED, B.DUES_OUT, B.RESERVED
            FROM ellrep.MSF100 A INNER JOIN ellrep.MSF170 B ON A.STOCK_CODE = B.STOCK_CODE
            WHERE A.CLASS in ('2','A','E','R') and B.STOCK_TYPE in ('6','7','8') and A.STOCK_STATUS in ('X','A')
            and B.INVT_STAT_CODE in ('2111','3121','3131','3141','3151','4141','5201','5202','5203','5221','5222','5223','5231','5321','5401','5501','6200','6201','6203','6204','6205','6221')
            GROUP BY B.STOCK_CODE, A.STOCK_STATUS, A.STOCK_SECTIONX1, A.ITEM_NAME, A.DESC_LINEX1, A.DESC_LINEX2, A.DESC_LINEX3, A.DESC_LINEX4,
            B.UNIT_OF_ISSUE, B.INVENT_COST_PR, B.CLASS, B.STOCK_TYPE, B.ROQ, B.DUES_IN, B.IN_TRANSIT, B.CONSIGN_ITRANS, B.TOTAL_PICKED,
            B.DUES_OUT, B.RESERVED;