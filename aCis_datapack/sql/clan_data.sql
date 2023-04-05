CREATE TABLE IF NOT EXISTS `clan_data` (
  `clan_id` INT NOT NULL DEFAULT 0,
  `clan_name` VARCHAR(20),
  `clan_level` INT NOT NULL DEFAULT 0,
  `reputation_score` INT NOT NULL DEFAULT 0,
  `hasCastle` TINYINT NOT NULL DEFAULT 0,
  `ally_id` INT NOT NULL DEFAULT 0,
  `ally_name` VARCHAR(20),
  `leader_id` INT NOT NULL DEFAULT 0,
  `new_leader_id` INT NOT NULL DEFAULT 0,
  `crest_id` INT NOT NULL DEFAULT 0,
  `crest_large_id` INT NOT NULL DEFAULT 0,
  `ally_crest_id` INT NOT NULL DEFAULT 0,
  `auction_bid_at` INT NOT NULL DEFAULT 0,
  `ally_penalty_expiry_time` BIGINT NOT NULL DEFAULT 0,
  `ally_penalty_type` INT NOT NULL DEFAULT 0,
  `char_penalty_expiry_time` BIGINT NOT NULL DEFAULT 0,
  `dissolving_expiry_time` BIGINT NOT NULL DEFAULT 0,
  `enabled` TINYINT NOT NULL DEFAULT 0,
  `notice` TEXT,
  `introduction` TEXT,
  PRIMARY KEY (`clan_id`),
  KEY `leader_id` (`leader_id`),
  KEY `ally_id` (`ally_id`)
);