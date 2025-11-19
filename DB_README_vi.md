
# README CSDL: Lottery + Dream (Giải mã giấc mơ)

Tài liệu này mô tả **chi tiết** từng bảng trong schema MySQL, ý nghĩa từng **trường/cột**, **quan hệ** giữa các bảng, và **cách INSERT dữ liệu** đúng chuẩn để hệ thống Chat (Stats/Dream) hoạt động trơn tru.

> Phiên bản schema tương ứng với file: `backend/src/main/resources/schema.sql` trong project.

---

## Mục lục
1. [Tổng quan & sơ đồ quan hệ](#tổng-quan--sơ-đồ-quan-hệ)  
2. [Bảng khu vực xổ số](#bảng-khu-vực-xổ-số)  
   - [draws](#1-draws)  
   - [results](#2-results)  
   - [n2](#3-n2)  
3. [Bảng từ điển giấc mơ](#bảng-từ-điển-giấc-mơ)  
   - [dream_symbols](#4-dream_symbols)  
   - [dream_synonyms](#5-dream_synonyms)  
   - [dream_symbol_n2](#6-dream_symbol_n2)  
   - [dream_symbol_n3 (tuỳ chọn)](#7-dream_symbol_n3-tuỳ-chọn)  
4. [Log giấc mơ & Chat](#log-giấc-mơ--chat)  
   - [dream_user_entries](#8-dream_user_entries)  
   - [chat_logs (tuỳ chọn)](#9-chat_logs-tuỳ-chọn)  
5. [Checklist INSERT dữ liệu mẫu](#checklist-insert-dữ-liệu-mẫu)  
6. [Câu lệnh kiểm tra nhanh](#câu-lệnh-kiểm-tra-nhanh)  
7. [Tips & Lưu ý quan trọng](#tips--lưu-ý-quan-trọng)

---

## Tổng quan & sơ đồ quan hệ

```
draws (1) ─────< results (n)

dream_symbols (1) ─────< dream_synonyms (n)
dream_symbols (1) ─────< dream_symbol_n2 (n)
dream_symbols (1) ─────< dream_symbol_n3 (n)

dream_user_entries (1) ─────< dream_log_symbols (n)   [*ghi log symbol phát hiện*]
dream_user_entries (1) ─────< dream_log_numbers (n)   [*ghi log số gợi ý*]
```

- Khu vực **xổ số** dùng cho thống kê (Stats).  
- Khu vực **giấc mơ** dùng để *giải mã* → gợi ý **lô 2 số** (và mở rộng 3 số nếu cần).  
- Hệ thống Dream có thể **kết hợp** thống kê nóng/lạnh từ `results` để chấm điểm.

---

## Bảng khu vực xổ số

### 1) `draws`
> Lưu **mỗi kỳ mở thưởng** (theo ngày + vùng + tỉnh/đài).

| Cột | Kiểu | Ràng buộc | Ý nghĩa |
|---|---|---|---|
| `id` | BIGINT PK AUTO_INCREMENT | PK | Khóa chính tự tăng; dùng ở `results.draw_id` |
| `region` | VARCHAR(4) NOT NULL |  | Vùng: `MB`, `MN`, `MT` |
| `province` | VARCHAR(64) NULL | UNIQUE chung | Tỉnh/đài (áp dụng MN/MT). MB có thể `NULL`, **hoặc** dùng `'MB'` để chống trùng |
| `draw_date` | DATE NOT NULL | UNIQUE chung | Ngày mở thưởng |
| `game` | VARCHAR(16) NOT NULL DEFAULT `'XSTT'` |  | Loại game (mặc định xổ số truyền thống) |
| `created_at` | TIMESTAMP DEFAULT `CURRENT_TIMESTAMP` |  | Thời điểm ghi dữ liệu |
| **UNIQUE** | (`region`, `province`, `draw_date`) | | Chặn trùng kỳ quay (xem *Lưu ý* phía dưới) |

**INSERT mẫu (MB – 1 đài/ngày):**
```sql
INSERT INTO draws(region, province, draw_date, game)
VALUES ('MB', NULL, '2025-11-18', 'XSTT');

-- Gợi ý chống trùng tốt hơn ở MB: dùng 'MB' thay vì NULL
INSERT INTO draws(region, province, draw_date, game)
VALUES ('MB', 'MB', '2025-11-18', 'XSTT');
```

**INSERT mẫu (MN – nhiều đài cùng ngày):**
```sql
INSERT INTO draws(region, province, draw_date, game)
VALUES ('MN', 'TPHCM', '2025-11-18', 'XSTT');
INSERT INTO draws(region, province, draw_date, game)
VALUES ('MN', 'DongThap', '2025-11-18', 'XSTT');
```

> **Lưu ý:** Nếu để `province=NULL` cho MB, constraint UNIQUE với `NULL` có thể **không chặn** trùng ngày. An toàn nhất là đặt `province='MB'`.

---

### 2) `results`
> Lưu **các giải & số trúng** của 1 kỳ (`draw_id`).

| Cột | Kiểu | Ý nghĩa |
|---|---|---|
| `id` | BIGINT PK AUTO_INCREMENT | Khóa chính |
| `draw_id` | BIGINT NOT NULL FK → `draws.id` | Kỳ quay |
| `prize_name` | VARCHAR(8) NOT NULL | Tên giải: `DB`, `G1`, `G2`, … `G8` |
| `seq` | TINYINT NOT NULL DEFAULT 1 | Thứ tự khi 1 giải có **nhiều số** |
| `number` | VARCHAR(10) NOT NULL | **Chuỗi số trúng**, giữ *số 0 đầu* |
| `digits` | TINYINT GENERATED ALWAYS AS (CHAR_LENGTH(number)) | **Generated**: số chữ số |
| `last2` | CHAR(2) GENERATED ALWAYS AS (RIGHT(number,2)) | **Generated**: 2 số cuối (dùng thống kê lô 2 số) |
| `last3` | CHAR(3) GENERATED ALWAYS AS (RIGHT(number,3)) | **Generated**: 3 số cuối |

**INSERT mẫu cho 1 kỳ (MB):**
```sql
-- Giả sử vừa chèn draws và lấy @draw_id
INSERT INTO results(draw_id, prize_name, seq, number) VALUES
(@draw_id, 'DB', 1, '12345'),  -- “đề” = last2 = '45'
(@draw_id, 'G1', 1, '56789'),
(@draw_id, 'G2', 1, '01234'),
(@draw_id, 'G2', 2, '98765'),  -- G2 có 2 số -> seq=1,2
(@draw_id, 'G7', 1, '23');     -- G7 thường 2 chữ số
```

> **Không cần** điền `digits/last2/last3`. MySQL tự tính.

---

### 3) `n2`
> Danh mục **00–99** để làm **universe** cho lô 2 số (thống kê hot/cold/overdue, so khớp…).

| Cột | Kiểu | Ý nghĩa |
|---|---|---|
| `n` | CHAR(2) PK | `'00'` … `'99'` |

**Seed 00..99 (chạy 1 lần):**
```sql
INSERT INTO n2(n)
SELECT LPAD(t.i,2,'0')
FROM (
  SELECT 0 i UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL
  SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL
  SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL
  SELECT 15 UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL
  SELECT 20 UNION ALL SELECT 21 UNION ALL SELECT 22 UNION ALL SELECT 23 UNION ALL SELECT 24 UNION ALL
  SELECT 25 UNION ALL SELECT 26 UNION ALL SELECT 27 UNION ALL SELECT 28 UNION ALL SELECT 29 UNION ALL
  SELECT 30 UNION ALL SELECT 31 UNION ALL SELECT 32 UNION ALL SELECT 33 UNION ALL SELECT 34 UNION ALL
  SELECT 35 UNION ALL SELECT 36 UNION ALL SELECT 37 UNION ALL SELECT 38 UNION ALL SELECT 39 UNION ALL
  SELECT 40 UNION ALL SELECT 41 UNION ALL SELECT 42 UNION ALL SELECT 43 UNION ALL SELECT 44 UNION ALL
  SELECT 45 UNION ALL SELECT 46 UNION ALL SELECT 47 UNION ALL SELECT 48 UNION ALL SELECT 49 UNION ALL
  SELECT 50 UNION ALL SELECT 51 UNION ALL SELECT 52 UNION ALL SELECT 53 UNION ALL SELECT 54 UNION ALL
  SELECT 55 UNION ALL SELECT 56 UNION ALL SELECT 57 UNION ALL SELECT 58 UNION ALL SELECT 59 UNION ALL
  SELECT 60 UNION ALL SELECT 61 UNION ALL SELECT 62 UNION ALL SELECT 63 UNION ALL SELECT 64 UNION ALL
  SELECT 65 UNION ALL SELECT 66 UNION ALL SELECT 67 UNION ALL SELECT 68 UNION ALL SELECT 69 UNION ALL
  SELECT 70 UNION ALL SELECT 71 UNION ALL SELECT 72 UNION ALL SELECT 73 UNION ALL SELECT 74 UNION ALL
  SELECT 75 UNION ALL SELECT 76 UNION ALL SELECT 77 UNION ALL SELECT 78 UNION ALL SELECT 79 UNION ALL
  SELECT 80 UNION ALL SELECT 81 UNION ALL SELECT 82 UNION ALL SELECT 83 UNION ALL SELECT 84 UNION ALL
  SELECT 85 UNION ALL SELECT 86 UNION ALL SELECT 87 UNION ALL SELECT 88 UNION ALL SELECT 89 UNION ALL
  SELECT 90 UNION ALL SELECT 91 UNION ALL SELECT 92 UNION ALL SELECT 93 UNION ALL SELECT 94 UNION ALL
  SELECT 95 UNION ALL SELECT 96 UNION ALL SELECT 97 UNION ALL SELECT 98 UNION ALL SELECT 99
) t;
```

---

## Bảng từ điển giấc mơ

### 4) `dream_symbols`
> Tập “biểu tượng” chuẩn hoá để quy chiếu cho các giấc mơ.

| Cột | Kiểu | Ràng buộc | Ý nghĩa |
|---|---|---|---|
| `id` | BIGINT PK AUTO_INCREMENT | PK | Khoá chính |
| `symbol` | VARCHAR(128) NOT NULL UNIQUE | Unique | Tên chuẩn (ví dụ `rắn`, `đám cưới`) |
| `category` | VARCHAR(32) NULL |  | Nhóm (`animal`, `event`, …) |
| `description` | TEXT NULL |  | Mô tả tự do |
| `active` | TINYINT(1) NOT NULL DEFAULT 1 |  | 1=đang dùng |
| `created_at` | TIMESTAMP DEFAULT CURRENT_TIMESTAMP |  | Thời điểm tạo |

**INSERT mẫu:**
```sql
INSERT INTO dream_symbols(symbol, category, description)
VALUES ('rắn', 'animal', 'biểu tượng rắn')
ON DUPLICATE KEY UPDATE description=VALUES(description);
```

---

### 5) `dream_synonyms`
> Các **từ khoá/phrases** để map từ text → `symbol_id`.

| Cột | Kiểu | Ràng buộc | Ý nghĩa |
|---|---|---|---|
| `id` | BIGINT PK AUTO_INCREMENT | PK |  |
| `symbol_id` | BIGINT NOT NULL FK → `dream_symbols.id` |  | Liên kết biểu tượng |
| `phrase` | VARCHAR(128) NOT NULL | UNIQUE (`symbol_id`, `phrase`) | Từ khoá (ví dụ: `rắn cắn`, `con rắn`) |

**INSERT mẫu (dùng SELECT để lấy id):**
```sql
INSERT INTO dream_synonyms(symbol_id, phrase)
SELECT id, 'rắn cắn' FROM dream_symbols WHERE symbol='rắn'
ON DUPLICATE KEY UPDATE phrase=VALUES(phrase);
```

---

### 6) `dream_symbol_n2`
> Mapping **symbol → lô 2 số** (có trọng số và ghi chú).

| Cột | Kiểu | Ràng buộc | Ý nghĩa |
|---|---|---|---|
| `id` | BIGINT PK AUTO_INCREMENT | PK |  |
| `symbol_id` | BIGINT NOT NULL FK → `dream_symbols.id` |  | Liên kết biểu tượng |
| `n2` | CHAR(2) NOT NULL | Unique (`symbol_id`, `n2`) | Số 2 chữ số `'00'..'99'` |
| `weight` | DOUBLE NOT NULL DEFAULT 1.0 |  | Độ ưu tiên/độ mạnh gợi ý |
| `note` | VARCHAR(255) NULL |  | Ghi chú |

**INSERT mẫu:**
```sql
INSERT INTO dream_symbol_n2(symbol_id, n2, weight, note)
SELECT id, '13', 1.0, 'từ điển nội bộ'
FROM dream_symbols WHERE symbol='rắn'
ON DUPLICATE KEY UPDATE weight=VALUES(weight), note=VALUES(note);
```

---

### 7) `dream_symbol_n3` (tuỳ chọn)
> Tương tự `dream_symbol_n2` nhưng dành cho **3 số** (nếu bạn dùng thêm).

| Cột | Kiểu | Ý nghĩa |
|---|---|---|
| `id` | BIGINT PK |  |
| `symbol_id` | BIGINT NOT NULL | FK |
| `n3` | CHAR(3) NOT NULL | `'000'..'999'` |
| `weight` | DOUBLE NOT NULL DEFAULT 1.0 |  |
| `note` | VARCHAR(255) NULL |  |

**INSERT mẫu:**
```sql
INSERT INTO dream_symbol_n3(symbol_id, n3, weight)
SELECT id, '123', 0.8 FROM dream_symbols WHERE symbol='rắn'
ON DUPLICATE KEY UPDATE weight=VALUES(weight);
```

---

## Log giấc mơ & Chat

### 8) `dream_user_entries`
> Log mỗi giấc mơ người dùng gửi vào **(được service Dream tự chèn)**.

| Cột | Kiểu | Ý nghĩa |
|---|---|---|
| `id` | BIGINT PK AUTO_INCREMENT | Khóa chính |
| `region` | VARCHAR(4) NULL | MB/MN/MT |
| `user_id` | VARCHAR(64) NULL | (Nếu bạn quản lý user) |
| `dream_text` | TEXT NOT NULL | Nội dung giấc mơ người dùng |
| `symbols_json` | JSON NULL | JSON list symbol match |
| `candidates_json` | JSON NULL | JSON list ứng viên số + điểm |
| `answer_text` | TEXT NULL | Câu trả lời tổng hợp |
| `created_at` | TIMESTAMP DEFAULT CURRENT_TIMESTAMP | |

> **Thông thường không cần tự INSERT** — service sẽ chèn. Nếu cần test nhanh:

```sql
INSERT INTO dream_user_entries(region, user_id, dream_text, symbols_json, candidates_json, answer_text)
VALUES ('MB', NULL, 'mơ thấy rắn cắn', JSON_ARRAY(JSON_OBJECT('symbol','rắn','phrase','rắn cắn')), JSON_ARRAY(), 'Gợi ý 2 số: 13, 31');
```

> Dựa trên `dream_user_entries.id`, hệ thống có thể chèn chi tiết vào 2 bảng dưới:

- `dream_log_symbols(dream_log_id, symbol_id, confidence)`  
- `dream_log_numbers(dream_log_id, n2, score, source, note)`

*(Hai bảng log chi tiết này không có sẵn trong schema v2 mặc định. Nếu muốn lưu chi tiết từng symbol/number kèm score, bạn có thể **mở rộng** thêm hai bảng này.)*

---

### 9) `chat_logs` (tuỳ chọn)
> Nếu bật ghi log chat (thêm code), có thể lưu ở đây.

| Cột | Kiểu | Ý nghĩa |
|---|---|---|
| `id` | BIGINT PK AUTO_INCREMENT |  |
| `type` | VARCHAR(16) NOT NULL | `'stats'` hoặc `'dream'` |
| `question` | TEXT NOT NULL | Câu hỏi người dùng |
| `sql_generated` | TEXT NULL | SQL do LLM sinh (nếu `stats`) |
| `row_count` | INT NULL | Số dòng kết quả |
| `answer` | TEXT NULL | Trả lời |
| `created_at` | TIMESTAMP DEFAULT CURRENT_TIMESTAMP | |

---

## Checklist INSERT dữ liệu mẫu

### A) Nạp 1 kỳ mở thưởng + kết quả
```sql
-- 1) Tạo draws
INSERT INTO draws(region, province, draw_date, game)
VALUES ('MB', 'MB', CURDATE(), 'XSTT');
SET @draw_id = LAST_INSERT_ID();

-- 2) Nạp results (mỗi giải 1..n số)
INSERT INTO results(draw_id, prize_name, seq, number) VALUES
(@draw_id, 'DB', 1, '12345'),
(@draw_id, 'G1', 1, '45678'),
(@draw_id, 'G2', 1, '01234'),
(@draw_id, 'G2', 2, '76543'),
(@draw_id, 'G7', 1, '12');
```

### B) Seed 00..99 cho `n2`
*(chạy 1 lần, xem đoạn trong mục [n2](#3-n2))*

### C) Thêm từ điển giấc mơ
```sql
-- 1) Biểu tượng
INSERT INTO dream_symbols(symbol, category, description)
VALUES ('rắn', 'animal', 'biểu tượng rắn')
ON DUPLICATE KEY UPDATE description=VALUES(description);

-- 2) Synonyms (từ khoá)
INSERT INTO dream_synonyms(symbol_id, phrase)
SELECT id, 'rắn cắn' FROM dream_symbols WHERE symbol='rắn'
ON DUPLICATE KEY UPDATE phrase=VALUES(phrase);

INSERT INTO dream_synonyms(symbol_id, phrase)
SELECT id, 'con rắn' FROM dream_symbols WHERE symbol='rắn'
ON DUPLICATE KEY UPDATE phrase=VALUES(phrase);

-- 3) Lô 2 số gợi ý
INSERT INTO dream_symbol_n2(symbol_id, n2, weight, note)
SELECT id, '13', 1.0, 'từ điển nội bộ' FROM dream_symbols WHERE symbol='rắn'
ON DUPLICATE KEY UPDATE weight=VALUES(weight), note=VALUES(note);

INSERT INTO dream_symbol_n2(symbol_id, n2, weight, note)
SELECT id, '31', 0.9, 'từ điển nội bộ' FROM dream_symbols WHERE symbol='rắn'
ON DUPLICATE KEY UPDATE weight=VALUES(weight), note=VALUES(note);
```

### D) Log 1 giấc mơ (test thủ công)
```sql
INSERT INTO dream_user_entries(region, user_id, dream_text, symbols_json, candidates_json, answer_text)
VALUES ('MB', NULL, 'mơ thấy rắn cắn', JSON_ARRAY(JSON_OBJECT('symbol','rắn','phrase','rắn cắn')), JSON_ARRAY(), 'Gợi ý 2 số: 13, 31');
```

---

## Câu lệnh kiểm tra nhanh

- **Xem 50 kết quả mới nhất**:
```sql
SELECT d.region, d.province, d.draw_date, r.prize_name, r.number, r.last2
FROM results r JOIN draws d ON d.id=r.draw_id
ORDER BY d.draw_date DESC, r.prize_name, r.seq
LIMIT 50;
```

- **Top lô 2 số 30 ngày gần đây (MB)**:
```sql
SELECT r.last2 AS n2, COUNT(*) AS freq
FROM results r JOIN draws d ON d.id=r.draw_id
WHERE d.region='MB' AND d.draw_date >= CURDATE() - INTERVAL 30 DAY
GROUP BY r.last2
ORDER BY freq DESC
LIMIT 10;
```

- **“Đề” (2 số cuối của DB) 90 ngày (MB)**:
```sql
SELECT r.last2 AS n2, COUNT(*) AS freq
FROM results r JOIN draws d ON d.id=r.draw_id
WHERE d.region='MB' AND r.prize_name='DB'
  AND d.draw_date >= CURDATE() - INTERVAL 90 DAY
GROUP BY r.last2
ORDER BY freq DESC
LIMIT 10;
```

- **Tra các mapping giấc mơ → lô 2 số**:
```sql
SELECT ds.symbol, dn.n2, dn.weight, dn.note
FROM dream_symbol_n2 dn JOIN dream_symbols ds ON ds.id=dn.symbol_id
ORDER BY ds.symbol, dn.weight DESC, dn.n2;
```

- **Tìm synonym khớp chứa 'rắn'**:
```sql
SELECT s.phrase, ds.symbol
FROM dream_synonyms s JOIN dream_symbols ds ON ds.id=s.symbol_id
WHERE s.phrase LIKE '%rắn%';
```

---

## Tips & Lưu ý quan trọng

- **MB**: để tránh trùng UNIQUE khi `province=NULL`, khuyến nghị dùng `province='MB'`.  
- **Giữ `number` là chuỗi** (`VARCHAR`) để **không mất số 0 đầu**.  
- **`digits/last2/last3` là cột generated** → không INSERT/UPDATE trực tiếp.  
- **Index** đã có (`idx_last2`, `idx_last3`, `idx_draw_prize`) hỗ trợ thống kê nhanh.  
- **Dictionary giấc mơ**: càng phong phú (synonyms, weight) → gợi ý **dream** càng tốt.  
- **Logging**: `dream_user_entries` được service chèn **tự động** mỗi khi người dùng chat “dream”. Bạn có thể bật thêm `chat_logs` nếu muốn audit mọi request.

---

**Hết.**
