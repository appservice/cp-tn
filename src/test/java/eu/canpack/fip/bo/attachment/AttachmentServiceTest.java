package eu.canpack.fip.bo.attachment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * CP S.A.
 * Created by lukasz.mochel on 18.09.2017.
 */

public class AttachmentServiceTest {

    public final MockitoRule mockitoRule = MockitoJUnit.rule();
    private AttachmentService attachmentService;
    @Mock
    private AttachmentRepository attachmentRepository;

    @Before
    public void setUp() throws Exception {
        this.attachmentService = new AttachmentService(attachmentRepository);

    }

    @Test
    public void upload() throws Exception {
    }

    @Test
    public void deleteAttachment() throws Exception {

//        Method method=attachmentService.getClass().getDeclaredMethod("deleteAttachment", Long.class);
//        method.setAccessible(true);
//        method.invoke(attachmentService, 12L);
        Constructor<Unsafe> unsafeConstructor = Unsafe.class.getDeclaredConstructor();
        unsafeConstructor.setAccessible(true);
        Unsafe unsafe = unsafeConstructor.newInstance();

        System.out.println(unsafe.addressSize());
    }

}
