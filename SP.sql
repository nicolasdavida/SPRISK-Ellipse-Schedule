IF EXISTS (SELECT 1 FROM sys.objects WHERE object_id = object_id('dbo.sp_respaldo'))
	SET NOEXEC OFF
	DROP PROCEDURE dbo.sp_respaldo
GO
CREATE PROCEDURE dbo.sp_respaldo
	as
	Declare @id as varchar(255)
	Declare @costo decimal(10, 2)
	Declare @stock int
	Declare @leadtime decimal(10, 0)
	Declare @row_cant int = 0
	SET NOCOUNT ON -- Importante dejarlo en ON para que no cuente y deje hacer el result set de Java

	DECLARE cursor_rmes CURSOR LOCAL FOR 
	SELECT stock_code, ISNULL(invent_cost_pr, 0), ISNULL(stock_total_act, 0), ISNULL(lead_time, 0) FROM dbo.tsrmes_rpto_oracle
	 
	OPEN cursor_rmes
	FETCH NEXT FROM cursor_rmes into @id,@costo,@stock,@leadtime
	 
	WHILE @@FETCH_STATUS=0
		BEGIN
			UPDATE dbo.tsrmes_repto SET costo = @costo, cant_stock = @stock, lead_time_comp = @leadtime WHERE ds_co = @id
			SET @row_cant = @row_cant + @@ROWCOUNT
			FETCH NEXT FROM cursor_rmes into @id,@costo,@stock,@leadtime
		END
		SELECT @row_cant as 'FILAS AFECTADAS'
	close cursor_rmes
	Deallocate cursor_rmes
GO

-- OTORGAR PERMISOS
USE cgssa_sandbox;
GRANT EXEC ON dbo.sp_respaldo TO PUBLIC

EXEC dbo.sp_respaldo


