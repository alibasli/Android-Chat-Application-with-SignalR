using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Social.DAL.Entities
{
   public class Post
    {
        [Key, DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public Guid PostID { get; set; }

        public Guid UserID { get; set; }

        public Guid AlbumID { get; set; }

        public Guid SharingID { get; set; }

        public string Title { get; set; }

        public string Body { get; set; }

        public DateTime PoestedDate { get; set; }

        public bool IsDeleted { get; set; }

        public virtual User User { get; set; }

    }
}
