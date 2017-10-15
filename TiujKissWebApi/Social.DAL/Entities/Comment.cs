using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Social.DAL.Entities
{
    public class Comment
    {
        [Key, DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public Guid CommentID { get; set; }

        public Guid SharingID { get; set; }

        public Guid UserID { get; set; }

        public DateTime DateLiking { get; set; }

        public DateTime DateEditing { get; set; }

        public string Content { get; set; }

        public string EditContent { get; set; }

        public bool IsDeleted { get; set; }

        public virtual User User { get; set; }

    }
}
