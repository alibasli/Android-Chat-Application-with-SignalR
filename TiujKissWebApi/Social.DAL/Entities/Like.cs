using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Social.DAL.Entities
{
  public  class Like
    {
        [Key, DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public Guid LikeID { get; set; }

        public Guid SharingID { get; set; }

        public Guid UserID { get; set; }

        public DateTime DateLiking { get; set; }

        public virtual User User { get; set; }

    }
}
