USE [DataBase]
GO
/****** Object:  Table [dbo].[产品]    Script Date: 2023/5/22 22:05:48 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[产品](
	[产品ID] [int] NOT NULL,
	[产品名称] [nvarchar](80) NOT NULL,
	[产品单位数量] [nvarchar](40) NOT NULL,
	[产品单价] [money] NOT NULL,
	[产品库存量] [smallint] NOT NULL,
	[产品订购量] [smallint] NOT NULL,
	[产品再订购量] [smallint] NULL,
	[产品中止] [bit] NULL,
	[产品类别ID] [int] NULL,
 CONSTRAINT [PK_产品表] PRIMARY KEY CLUSTERED 
(
	[产品ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[订单]    Script Date: 2023/5/22 22:05:48 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[订单](
	[订单ID] [int] NOT NULL,
	[订单订购日期] [datetime] NULL,
	[订单发货日期] [datetime] NULL,
	[订单到货日期] [datetime] NULL,
	[订单货款确认日期] [datetime] NULL,
	[订单运货费] [money] NULL,
	[订单货主名称] [nvarchar](80) NULL,
	[订单货主详细地址] [nvarchar](120) NULL,
	[订单货主所在国家] [nvarchar](30) NULL,
	[订单货主所在城市] [nvarchar](30) NULL,
	[订单货主所在地区] [nvarchar](30) NULL,
	[订单货主所在地邮政编码] [nvarchar](20) NOT NULL,
	[订单折扣] [money] NULL,
	[客户ID] [nvarchar](10) NULL,
	[雇员ID] [int] NULL,
	[供应商ID] [int] NULL,
 CONSTRAINT [PK_订单表] PRIMARY KEY CLUSTERED 
(
	[订单ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[订单明细]    Script Date: 2023/5/22 22:05:48 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[订单明细](
	[订单ID] [int] NOT NULL,
	[产品ID] [int] NOT NULL,
	[数量] [smallint] NOT NULL,
	[单项产品总金额] [money] NULL,
 CONSTRAINT [PK_订单详细] PRIMARY KEY CLUSTERED 
(
	[订单ID] ASC,
	[产品ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[供应商]    Script Date: 2023/5/22 22:05:48 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[供应商](
	[供应商ID] [int] NOT NULL,
	[供应商公司名称] [nvarchar](80) NOT NULL,
	[供应商联系人姓名] [nvarchar](60) NOT NULL,
	[供应商联系人职务] [nvarchar](60) NULL,
	[供应商详细地址] [nvarchar](120) NOT NULL,
	[供应商所在国家] [nvarchar](30) NULL,
	[供应商所在城市] [nvarchar](30) NULL,
	[供应商所在地区] [nvarchar](30) NULL,
	[供应商所在地邮政编码] [nvarchar](20) NOT NULL,
	[供应商电话] [nvarchar](48) NOT NULL,
	[供应商传真] [nvarchar](48) NULL,
	[供应商网站主页] [ntext] NULL,
	[供应商登陆密码] [nvarchar](30) NOT NULL,
 CONSTRAINT [PK_供应商表] PRIMARY KEY CLUSTERED 
(
	[供应商ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[供应商-产品关系]    Script Date: 2023/5/22 22:05:48 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[供应商-产品关系](
	[产品ID] [int] NOT NULL,
	[供应商ID] [int] NOT NULL,
	[产品当前库存量] [smallint] NOT NULL,
 CONSTRAINT [PK_供应商-产品关系] PRIMARY KEY CLUSTERED 
(
	[产品ID] ASC,
	[供应商ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[雇员]    Script Date: 2023/5/22 22:05:48 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[雇员](
	[雇员ID] [int] NOT NULL,
	[雇员姓氏] [nvarchar](40) NOT NULL,
	[雇员名字] [nvarchar](20) NOT NULL,
	[雇员职务] [nvarchar](60) NOT NULL,
	[雇员尊称] [nvarchar](50) NULL,
	[雇员出生日期] [datetime] NOT NULL,
	[雇员雇佣日期] [datetime] NOT NULL,
	[雇员详细地址] [nvarchar](120) NOT NULL,
	[雇员所在国家] [nvarchar](30) NULL,
	[雇员所在城市] [nvarchar](30) NULL,
	[雇员所在地区] [nvarchar](30) NULL,
	[雇员所在地邮政编码] [nvarchar](20) NOT NULL,
	[雇员家庭电话] [nvarchar](48) NULL,
	[雇员电话分机] [nvarchar](8) NULL,
	[雇员照片] [nvarchar](max) NULL,
	[雇员备注] [ntext] NULL,
	[雇员上级] [int] NULL,
	[雇员登陆密码] [nvarchar](30) NOT NULL,
 CONSTRAINT [PK_雇员] PRIMARY KEY CLUSTERED 
(
	[雇员ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[客户]    Script Date: 2023/5/22 22:05:48 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[客户](
	[客户ID] [nvarchar](10) NOT NULL,
	[客户公司名称] [nvarchar](80) NOT NULL,
	[联系人姓名] [nvarchar](60) NOT NULL,
	[联系人职务] [nvarchar](60) NULL,
	[客户详细地址] [nvarchar](120) NOT NULL,
	[客户所在国家] [nvarchar](30) NULL,
	[客户所在城市] [nvarchar](30) NULL,
	[客户所在地区] [nvarchar](30) NULL,
	[客户所在地邮政编码] [nvarchar](20) NOT NULL,
	[客户电话] [nvarchar](48) NOT NULL,
	[客户传真] [nvarchar](48) NULL,
	[客户登录密码] [nvarchar](30) NOT NULL,
	[客户总积分] [money] NULL,
 CONSTRAINT [PK_客户] PRIMARY KEY CLUSTERED 
(
	[客户ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[类别]    Script Date: 2023/5/22 22:05:48 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[类别](
	[类别ID] [int] NOT NULL,
	[类别名称] [nvarchar](30) NOT NULL,
	[类别说明] [ntext] NOT NULL,
	[类别图片] [nvarchar](max) NULL,
 CONSTRAINT [PK_类别表] PRIMARY KEY CLUSTERED 
(
	[类别ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Index [供应商ID索引]    Script Date: 2023/5/22 22:05:48 ******/
CREATE NONCLUSTERED INDEX [供应商ID索引] ON [dbo].[订单]
(
	[供应商ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 80, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [雇员ID索引]    Script Date: 2023/5/22 22:05:48 ******/
CREATE NONCLUSTERED INDEX [雇员ID索引] ON [dbo].[订单]
(
	[雇员ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 80, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [客户ID索引]    Script Date: 2023/5/22 22:05:48 ******/
CREATE NONCLUSTERED INDEX [客户ID索引] ON [dbo].[订单]
(
	[客户ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 80, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
ALTER TABLE [dbo].[产品] ADD  CONSTRAINT [DF_产品_产品库存量]  DEFAULT ((0)) FOR [产品库存量]
GO
ALTER TABLE [dbo].[产品] ADD  CONSTRAINT [DF_产品_产品订购量]  DEFAULT ((0)) FOR [产品订购量]
GO
ALTER TABLE [dbo].[产品] ADD  CONSTRAINT [DF_产品_产品再订购量]  DEFAULT ((0)) FOR [产品再订购量]
GO
ALTER TABLE [dbo].[产品] ADD  CONSTRAINT [DF_产品_产品中止]  DEFAULT ((1)) FOR [产品中止]
GO
ALTER TABLE [dbo].[产品] ADD  CONSTRAINT [DF_产品_产品类别ID]  DEFAULT ((0)) FOR [产品类别ID]
GO
ALTER TABLE [dbo].[供应商] ADD  CONSTRAINT [DF_供应商_供应商登陆密码]  DEFAULT ((12345678)) FOR [供应商登陆密码]
GO
ALTER TABLE [dbo].[供应商-产品关系] ADD  CONSTRAINT [DF_供应商-产品关系_产品当前库存量]  DEFAULT ((0)) FOR [产品当前库存量]
GO
ALTER TABLE [dbo].[雇员] ADD  CONSTRAINT [DF_雇员_雇员登陆密码]  DEFAULT ((12345678)) FOR [雇员登陆密码]
GO
ALTER TABLE [dbo].[客户] ADD  CONSTRAINT [DF_客户_客户登录密码_1]  DEFAULT ((12345678)) FOR [客户登录密码]
GO
ALTER TABLE [dbo].[客户] ADD  CONSTRAINT [DF_客户_客户总积分]  DEFAULT ((0)) FOR [客户总积分]
GO
ALTER TABLE [dbo].[产品]  WITH CHECK ADD  CONSTRAINT [产品类别ID外键约束] FOREIGN KEY([产品类别ID])
REFERENCES [dbo].[类别] ([类别ID])
GO
ALTER TABLE [dbo].[产品] CHECK CONSTRAINT [产品类别ID外键约束]
GO
ALTER TABLE [dbo].[订单]  WITH CHECK ADD  CONSTRAINT [供应商ID外键约束] FOREIGN KEY([供应商ID])
REFERENCES [dbo].[供应商] ([供应商ID])
GO
ALTER TABLE [dbo].[订单] CHECK CONSTRAINT [供应商ID外键约束]
GO
ALTER TABLE [dbo].[订单]  WITH CHECK ADD  CONSTRAINT [雇员ID外键约束] FOREIGN KEY([雇员ID])
REFERENCES [dbo].[雇员] ([雇员ID])
GO
ALTER TABLE [dbo].[订单] CHECK CONSTRAINT [雇员ID外键约束]
GO
ALTER TABLE [dbo].[订单]  WITH CHECK ADD  CONSTRAINT [客户ID外键约束] FOREIGN KEY([客户ID])
REFERENCES [dbo].[客户] ([客户ID])
GO
ALTER TABLE [dbo].[订单] CHECK CONSTRAINT [客户ID外键约束]
GO
ALTER TABLE [dbo].[订单明细]  WITH NOCHECK ADD  CONSTRAINT [产品ID外键约束] FOREIGN KEY([产品ID])
REFERENCES [dbo].[产品] ([产品ID])
ON UPDATE CASCADE
ON DELETE CASCADE
NOT FOR REPLICATION 
GO
ALTER TABLE [dbo].[订单明细] NOCHECK CONSTRAINT [产品ID外键约束]
GO
ALTER TABLE [dbo].[订单明细]  WITH NOCHECK ADD  CONSTRAINT [订单ID外键约束] FOREIGN KEY([订单ID])
REFERENCES [dbo].[订单] ([订单ID])
ON UPDATE CASCADE
ON DELETE CASCADE
NOT FOR REPLICATION 
GO
ALTER TABLE [dbo].[订单明细] NOCHECK CONSTRAINT [订单ID外键约束]
GO
ALTER TABLE [dbo].[供应商-产品关系]  WITH CHECK ADD  CONSTRAINT [产品ID外键约束_供应商-产品关系] FOREIGN KEY([产品ID])
REFERENCES [dbo].[产品] ([产品ID])
GO
ALTER TABLE [dbo].[供应商-产品关系] CHECK CONSTRAINT [产品ID外键约束_供应商-产品关系]
GO
ALTER TABLE [dbo].[供应商-产品关系]  WITH CHECK ADD  CONSTRAINT [供应商ID外键约束_供应商-产品关系] FOREIGN KEY([供应商ID])
REFERENCES [dbo].[供应商] ([供应商ID])
GO
ALTER TABLE [dbo].[供应商-产品关系] CHECK CONSTRAINT [供应商ID外键约束_供应商-产品关系]
GO
ALTER TABLE [dbo].[产品]  WITH CHECK ADD  CONSTRAINT [产品ID约束] CHECK  (([产品ID]>(0)))
GO
ALTER TABLE [dbo].[产品] CHECK CONSTRAINT [产品ID约束]
GO
ALTER TABLE [dbo].[产品]  WITH CHECK ADD  CONSTRAINT [产品库存量约束] CHECK  (([产品库存量]>=(0)))
GO
ALTER TABLE [dbo].[产品] CHECK CONSTRAINT [产品库存量约束]
GO
ALTER TABLE [dbo].[订单]  WITH CHECK ADD  CONSTRAINT [订单ID约束] CHECK  (([订单ID]>=(0)))
GO
ALTER TABLE [dbo].[订单] CHECK CONSTRAINT [订单ID约束]
GO
ALTER TABLE [dbo].[供应商]  WITH CHECK ADD  CONSTRAINT [供应商ID约束] CHECK  (([供应商ID]>=(0)))
GO
ALTER TABLE [dbo].[供应商] CHECK CONSTRAINT [供应商ID约束]
GO
ALTER TABLE [dbo].[供应商]  WITH CHECK ADD  CONSTRAINT [供应商电话约束] CHECK  ((len([供应商电话])>=(10)))
GO
ALTER TABLE [dbo].[供应商] CHECK CONSTRAINT [供应商电话约束]
GO
ALTER TABLE [dbo].[供应商]  WITH CHECK ADD  CONSTRAINT [供应商所在地邮政编码约束] CHECK  ((len([供应商所在地邮政编码])>=(4) AND len([供应商所在地邮政编码])<=(20)))
GO
ALTER TABLE [dbo].[供应商] CHECK CONSTRAINT [供应商所在地邮政编码约束]
GO
ALTER TABLE [dbo].[供应商]  WITH CHECK ADD  CONSTRAINT [供应商详细地址] CHECK  ((len([供应商详细地址])>=(4)))
GO
ALTER TABLE [dbo].[供应商] CHECK CONSTRAINT [供应商详细地址]
GO
ALTER TABLE [dbo].[客户]  WITH CHECK ADD  CONSTRAINT [客户ID约束] CHECK  ((len([客户ID])=(5)))
GO
ALTER TABLE [dbo].[客户] CHECK CONSTRAINT [客户ID约束]
GO
ALTER TABLE [dbo].[客户]  WITH CHECK ADD  CONSTRAINT [客户电话约束] CHECK  ((len([客户电话])>=(10)))
GO
ALTER TABLE [dbo].[客户] CHECK CONSTRAINT [客户电话约束]
GO
ALTER TABLE [dbo].[类别]  WITH CHECK ADD  CONSTRAINT [类别ID约束] CHECK  (([类别ID]>(0)))
GO
ALTER TABLE [dbo].[类别] CHECK CONSTRAINT [类别ID约束]
GO
ALTER TABLE [dbo].[类别]  WITH CHECK ADD  CONSTRAINT [类别名称约束] CHECK  ((len([类别名称])>=(1)))
GO
ALTER TABLE [dbo].[类别] CHECK CONSTRAINT [类别名称约束]
GO
/****** Object:  Trigger [dbo].[two]    Script Date: 2023/5/22 22:05:48 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create trigger [dbo].[two]
on [dbo].[订单] 
after insert
as 
begin
	declare @订单ID int;
	declare @总价 money; 
	declare @折扣 money; 
	declare @邮政编码 nvarchar(20);
	set @邮政编码=(SELECT 订单货主所在地邮政编码 FROM inserted);
	set @订单ID=(SELECT 订单ID FROM inserted);
	set @总价=(
	select  sum(单项产品总金额)
	from dbo.订单明细
	where dbo.订单明细.订单ID=@订单ID);
	if(@总价<1000) set @折扣=0;
	if(@总价>=1000 and @总价<5000)set @折扣=@总价*0.01;
	if(@总价>=5000 and @总价<10000)set @折扣=@总价*0.02;
	if(@总价>=10000)set @折扣=@总价*0.03;
	update  dbo.订单
	set 订单折扣=@折扣;
	
	/*update  dbo.订单
	set 客户ID=(select 客户ID
				from dbo.客户
				where dbo.客户.客户所在地邮政编码=@邮政编码);*/

end;
GO
ALTER TABLE [dbo].[订单] ENABLE TRIGGER [two]
GO
/****** Object:  Trigger [dbo].[one]    Script Date: 2023/5/22 22:05:49 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE trigger [dbo].[one] on [dbo].[订单明细] 
after insert
as 
begin
	declare @产品ID int;
	declare @订单ID int;
	declare @数量 smallint;
	declare @单价 money; 
	set @产品ID=(SELECT 产品ID FROM inserted);
	set @订单ID=(SELECT 订单ID FROM inserted);
	set @数量=(SELECT 数量 FROM inserted);
	set @单价=(select 产品单价 from dbo.产品 where dbo.产品.产品ID=@产品ID );
	update  dbo.订单明细 
	set 单项产品总金额=@单价*@数量 where 订单ID=@订单ID and 产品ID=@产品ID;
end;
GO
ALTER TABLE [dbo].[订单明细] ENABLE TRIGGER [one]
GO
