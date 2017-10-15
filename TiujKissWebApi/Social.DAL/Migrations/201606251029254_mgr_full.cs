namespace Social.DAL.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class mgr_full : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.Post",
                c => new
                    {
                        PostID = c.Guid(nullable: false, identity: true),
                        UserID = c.Guid(nullable: false),
                        AlbumID = c.Guid(nullable: false),
                        SharingID = c.Guid(nullable: false),
                        Title = c.String(),
                        Body = c.String(),
                        PoestedDate = c.DateTime(nullable: false),
                        IsDeleted = c.Boolean(nullable: false),
                    })
                .PrimaryKey(t => t.PostID)
                .ForeignKey("dbo.User", t => t.UserID, cascadeDelete: true)
                .Index(t => t.UserID);
            
            DropColumn("dbo.User", "Weight");
            DropColumn("dbo.User", "Height");
        }
        
        public override void Down()
        {
            AddColumn("dbo.User", "Height", c => c.Double(nullable: false));
            AddColumn("dbo.User", "Weight", c => c.Double(nullable: false));
            DropForeignKey("dbo.Post", "UserID", "dbo.User");
            DropIndex("dbo.Post", new[] { "UserID" });
            DropTable("dbo.Post");
        }
    }
}
