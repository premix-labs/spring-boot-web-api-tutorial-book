---
title: 33 - Transaction
description: ใช้ @Transactional กับ operation ที่ต้องสำเร็จหรือ rollback พร้อมกัน
---

## เป้าหมายของบท

บทนี้จะอธิบาย `@Transactional` จากตัวอย่าง admin action ที่เปลี่ยนข้อมูลผู้ใช้และบันทึก audit log

หลังจบบทนี้ผู้อ่านควรเข้าใจ:

- transaction คืออะไร
- ทำไม write หลาย table ควรอยู่ใน transaction เดียวกัน
- ใช้ `@Transactional` ที่ service layer อย่างไร
- read-only transaction ใช้เมื่อไร
- self-invocation คือปัญหาอะไร

## Transaction คืออะไร

Transaction คือขอบเขตของ database operation ที่ต้องสำเร็จทั้งชุด หรือถ้ามี error ต้อง rollback ทั้งชุด

ตัวอย่าง:

```text
1. เปลี่ยน user status จาก ACTIVE เป็น INACTIVE
2. บันทึก audit log ว่า admin คนไหนเป็นคนเปลี่ยน
```

ถ้า step 1 สำเร็จแต่ step 2 ล้มเหลว ระบบจะอยู่ในสถานะที่ตรวจสอบย้อนหลังไม่ได้ ดังนั้นสอง step นี้ควรอยู่ใน transaction เดียวกัน

## ใช้กับ service layer

ตัวอย่างใน `AdminUserService`:

```java
@Transactional
public UserResponse changeStatus(
        Long id,
        ChangeStatusRequest request,
        String actorEmail
) {
    User actor = findActor(actorEmail);
    User target = findUser(id);

    UserStatus oldStatus = target.getStatus();
    target.setStatus(request.status());

    auditLogService.record(
            "CHANGE_STATUS",
            actor.getId(),
            target.getId(),
            oldStatus.name(),
            request.status().name()
    );

    return toResponse(target);
}
```

ใน method นี้มีทั้ง update `users` และ insert `audit_logs` ถ้า audit log บันทึกไม่ได้ การเปลี่ยน status ควรถูก rollback

## ทำไมไม่ต้องเรียก save หลัง setStatus

เมื่อ entity ถูกโหลดใน transaction:

```java
User target = findUser(id);
```

แล้วเราแก้:

```java
target.setStatus(request.status());
```

JPA จะ track entity นี้อยู่ เมื่อ transaction จบ Hibernate จะ flush การเปลี่ยนแปลงลง database ให้

นี่เรียกว่า dirty checking

## Read-only transaction

method ที่อ่านข้อมูลอย่างเดียวอาจใส่:

```java
@Transactional(readOnly = true)
public PageResponse<UserResponse> findUsers(...) {
    // query only
}
```

ข้อดีคือสื่อเจตนาชัด และบาง provider/database อาจ optimize ได้

## Rollback เกิดเมื่อไร

โดยทั่วไป Spring จะ rollback เมื่อเกิด unchecked exception เช่น:

- `RuntimeException`
- custom exception ที่ extends `RuntimeException`
- `IllegalStateException`

ตัวอย่าง:

```java
throw new IllegalStateException("Admin cannot deactivate own account");
```

ถ้า exception เกิดใน transaction การเปลี่ยนแปลงก่อนหน้าจะ rollback

## ข้อควรระวังเรื่อง self-invocation

ถ้า method ใน class เดียวกันเรียก method ที่มี `@Transactional` ผ่าน `this` transaction อาจไม่ทำงานตามที่คิด เพราะ Spring ใช้ proxy

ตัวอย่างที่ควรหลีกเลี่ยง:

```java
public void outer() {
    this.innerTransactionalMethod();
}

@Transactional
public void innerTransactionalMethod() {
}
```

แนวทางง่ายสำหรับมือใหม่คือวาง `@Transactional` ที่ public service method ที่ถูก controller เรียกโดยตรง

## ใช้เมื่อไร

ควรใช้เมื่อ:

- write หลาย table ใน operation เดียว
- ต้องอ่าน entity แล้วแก้ใน transaction เดียว
- ต้องการ rollback เมื่อ action สำคัญล้มเหลว
- ต้องการ consistency ระหว่าง business data กับ audit log

ไม่ควรใส่แบบสุ่มทุก method เพราะจะทำให้ขอบเขต transaction ยาวเกินจำเป็น

## แบบฝึกหัดท้ายบท

1. ใส่ `@Transactional` ให้ `changeRole`
2. ใส่ `@Transactional` ให้ `changeStatus`
3. ใส่ `@Transactional(readOnly = true)` ให้ method list users
4. ลอง throw error หลัง set status แล้วตรวจว่า status rollback หรือไม่
5. อธิบายด้วยตัวเองว่า dirty checking คืออะไร
