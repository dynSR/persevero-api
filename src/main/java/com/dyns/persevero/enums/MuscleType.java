package com.dyns.persevero.enums;

/**
 * Represents specific muscles within the human body.
 * <p>
 * Belongs to a major muscle groups.
 * @see MuscleGroupType
 */
public enum MuscleType {

    NONE,

    // LOWER BODY (Legs and lower posterior chain)

    /**
     * The quadriceps muscles, located at the front of the thigh, responsible for knee extension.
     */
    QUADRICEPS,

    /**
     * The hamstrings, located at the back of the thigh, responsible for knee flexion and hip extension.
     */
    HAMSTRINGS,

    /**
     * The gluteal muscles (gluteus maximus, medius, and minimus), crucial for hip movement and stability.
     */
    GLUTES,

    /**
     * The adductors, located on the inner thigh, responsible for bringing the legs toward the midline of the body.
     */
    ADDUCTORS,

    /**
     * The calf muscles (gastrocnemius and soleus), responsible for plantar flexion of the foot.
     */
    CALVES,

    // UPPER BODY (Chest, back, shoulders, and arms)

    /**
     * The chest muscles (pectoralis major and minor), responsible for pressing movements.
     */
    CHEST,

    /**
     * The upper back muscles, including the trapezius and rhomboids, responsible for scapular movement and posture.
     */
    UPPER_BACK,

    /**
     * The lower back muscles (erector spinae, quadratus lumborum), crucial for spinal stability and extension.
     */
    LOWER_BACK,

    /**
     * The latissimus dorsi, a large muscle in the back, responsible for pulling movements.
     */
    LATISSIMUS_DORSI,

    /**
     * The deltoid muscles, responsible for shoulder abduction, flexion, and extension.
     */
    SHOULDERS,

    /**
     * The biceps brachii, responsible for elbow flexion and forearm supination.
     */
    BICEPS,

    /**
     * The triceps brachii, responsible for elbow extension.
     */
    TRICEPS,

    /**
     * The forearm muscles, responsible for wrist flexion, extension, and grip strength.
     */
    FOREARMS,

    // CORE (Abdominal and stabilizing muscles)

    /**
     * The rectus abdominis, also known as the "six-pack," responsible for spinal flexion.
     */
    RECTUS_ABDOMINIS,

    /**
     * The oblique muscles (internal and external), responsible for trunk rotation and lateral flexion.
     */
    OBLIQUES,

    /**
     * The transverse abdominis, a deep core muscle essential for core stability and intra-abdominal pressure.
     */
    TRANSVERSE_ABDOMINIS,

    /**
     * The serratus anterior, located on the side of the ribcage, responsible for scapular protraction and stability.
     */
    SERRATUS_ANTERIOR

}
