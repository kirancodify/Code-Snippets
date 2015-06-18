/*
 * main1.c
 *
 *  Created on: 17 Feb 2012
 *      Author: Emre Ilke Cosar
 */







/* Created by: EIC*/

/***** file main.c *****/

/* Standard includes. */
#include <stdlib.h>
#include <signal.h>
#include <string.h>

/* Scheduler includes. */
#include "FreeRTOS.h"
#include "task.h"
#include "queue.h"
#include "bus.h"
#include "gpio.h"
#include "debug.h"
#include "socket.h"
#include "rf.h"
#include "adc.h"
#include "buffer.h"
#include "flash.h"

/* Driver for the SHT7 sensor */
#include "SHT7_driver.h"

/* TASKS declaration */
static void Broadcast_Measurements(void *pvParameters);

/* Functions declaration */
uint16_t measure_hum(void);
uint16_t measure_temp(void);


void normal_broadcast(void);
void broadcast_alarm(void);
void get_min_max(void);

/* Constants declaration */
#define TUTORIAL1	10
#define NORMAL_MODE	33
#define ALARM_MODE      44
#define NORMAL_MODE_SAMPLES 4
#define SAMPLE_INTERVAL	2000
#define THRESHOLD_TEMP	7000 //This corresponds approximately to 30 degrees Celsius
#define THRESHOLD_HUM	1000 //This corresponds approximately to 33% relative humidity
//define your node ID number
# define NODEID  4


/* Variables declaration */
portTickType xTimeStamp;
/* Variables for storing temperature and humidity*/
uint16_t current_temp, current_hum;


//Create your own variables in this space
//unsigned integer 8 bits: uint8_t
//unsigned integer 16 bits: uint16_t
uint16_t max_temp,min_temp,max_hum,min_hum,sum_temp,sum_hum,array1[4], array2[4];
int i,count=0;




//DO NOT MODIFY THIS PART
/* Sockets declaration */
socket_t *Radio_Socket=0;

/* Buffers declaration */
buffer_t *T_Buffer;

/* Ports definition */
#define	BROADCAST_PORT_NUM 10

#define SCK_UP			P6OUT |= 0x04
#define SCK_DOWN		P6OUT &= 0xFB
#define DATA_UP			P6OUT |= 0x08
#define DATA_DOWN		P6OUT &= 0xF7

/* Addresses declaration */
sockaddr_t Broadcast_Add =
{
	ADDR_802_15_4_PAN_LONG,
	{ 0xFF, 0xFF, 0xFF, 0xFF,
	  0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF },
	BROADCAST_PORT_NUM
};





/* main */
int main( void )
{
  	/* Initializes the leds */
		LED_INIT();
		if (bus_init() == pdFALSE)
		{
		}
		/* Initializes the debug window */
		debug_init(115200);
		stack_init();
		/* Creates the tasks */
		xTaskCreate(Broadcast_Measurements,"MEAS_BROADCAST",256,NULL,(tskIDLE_PRIORITY+1),NULL);
		/* Starts the scheduler */
		vTaskStartScheduler();
		return 0;
}

static void Broadcast_Measurements (void *pvParameters)
{
		LED1_ON();
		vTaskDelay(1000);
		LED1_OFF();

		//define the radio socket and then bind it to the broadcast address

		Radio_Socket = socket(MODULE_CUDP,0); 	/* Socket creation */
		socket_bind(Radio_Socket, &Broadcast_Add); 	/* Socket bind */


		//Get the initial time stamp before the task enters the infinite for loop
		xTimeStamp=xTaskGetTickCount();
                
                if (measure_temp > THRESHOLD_TEMP && measure_hum > THRESHOLD_HUM )
                {
                
		for ( ;; )
		{
			current_temp=measure_temp();
			current_hum=measure_hum();
			debug_printf("temperature: %d  humidity %d \r\n",current_temp,current_hum);
			T_Buffer=socket_buffer_get(Radio_Socket);
			if (T_Buffer)
			{
				//Packet type
				buffer_push_uint8(T_Buffer, TUTORIAL1);
				//Node  id
				buffer_push_uint8(T_Buffer, NODE_ID);
				
				//Mode Type
				buffer_push_uint8(T_Buffer, ALARM_MODE);

				buffer_push_uint8(T_Buffer, current_temp); 			//8 LS bits
				buffer_push_uint8(T_Buffer, (current_temp>>8)); //8 MS bits

				buffer_push_uint8(T_Buffer, current_hum); 			//8 LS bits
				buffer_push_uint8(T_Buffer, (current_hum>>8)); 	//8 MS bits
				if (socket_sendto(Radio_Socket,&Broadcast_Add,T_Buffer) == pdTRUE)
				{
					debug("packet sent\r\n");

				}
				else
				{
					socket_buffer_free(T_Buffer); /*Free the buffer*/
					T_Buffer=0;
					debug("packet not sent\r\n");
				}
			}

			/*Wait*/
			vTaskDelayUntil(&xTimeStamp,SAMPLE_INTERVAL);
		}
		 else 
                {
                
		for ( ;; )
		{
			for ( count = 0 ; count < NORMAL_MODE_SAMPLES ; count++ )
 			{
    			array1[count]=measure_temp();
    			min_temp = array1[0];
    			max_temp = array1[0];
    			sum_temp = array1[0];
    			for ( count = 1 ; count < 4 ; count++ ) 
   				 {
       					 if ( array1[count] < min_temp ) 
        				{
          				 min_temp = array1[count];
					}
        				
        				if ( array1[count] > max_temp ) 
        				{
          				 max_temp = array1[count];
        				}	
        				
    				} 
    				sum_temp=sum_temp + array1[count];
			}
			for ( count = 0 ; count < NORMAL_MODE_SAMPLES ; count++ )
 			{
    			array2[count]=measure_hum();
    			min_hum = array2[0];
    			max_hum = array2[0];
    			sum_hum = array2[0];
    			for ( count = 1 ; count < 4 ; count++ ) 
   				 {
       					 if ( array2[count] < min_hum ) 
        				{
          				 min_hum = array2[count];
					}
        				
        				if ( array2[count] > max_hum ) 
        				{
          				 max_hum = array2[count];
        				}	
        				
    				}
				sum_hum=sum_hum + array2[count];
			}
			
					
			debug_printf("temperature: %d  humidity %d \r\n",current_temp,current_hum);
			T_Buffer=socket_buffer_get(Radio_Socket);
			if (T_Buffer)
			{
				//Packet type
				buffer_push_uint8(T_Buffer, TUTORIAL1);
				//Node  id
				buffer_push_uint8(T_Buffer, NODE_ID);
				
				//Mode Type
				buffer_push_uint8(T_Buffer, NORMAL_MODE);
				
				buffer_push_uint8(T_Buffer, NORMAL_MODE_SAMPLES);

				buffer_push_uint8(T_Buffer, min_temp); 			//8 LS bits
				buffer_push_uint8(T_Buffer, (min_temp>>8)); //8 MS bits
				
				buffer_push_uint8(T_Buffer, max_temp); 			//8 LS bits
				buffer_push_uint8(T_Buffer, (max_temp>>8)); //8 MS bits
				
				buffer_push_uint8(T_Buffer, sum_temp); 			//8 LS bits
				buffer_push_uint8(T_Buffer, (sum_temp>>8)); 	//8 MS bitsb
				
				buffer_push_uint8(T_Buffer, min_hum); 			//8 LS bits
				buffer_push_uint8(T_Buffer, (min_hum>>8)); 	//8 MS bits
				
				buffer_push_uint8(T_Buffer, max_hum); 			//8 LS bits
				buffer_push_uint8(T_Buffer, (max_hum>>8)); 	//8 MS bits
				
				buffer_push_uint8(T_Buffer, sum_hum); 			//8 LS bits
				buffer_push_uint8(T_Buffer, (sum_hum>>8)); 	//8 MS bits
				
				if (socket_sendto(Radio_Socket,&Broadcast_Add,T_Buffer) == pdTRUE)
				{
					debug("packet sent\r\n");

				}
				else
				{
					socket_buffer_free(T_Buffer); /*Free the buffer*/
					T_Buffer=0;
					debug("packet not sent\r\n");
				}
			}

			/*Wait*/
			vTaskDelayUntil(&xTimeStamp,SAMPLE_INTERVAL);
		}
		
	}
}

uint16_t measure_temp()
{
	uint16_t temp;
	/*Temperature acquisition*/
	P6SEL &= 0x00; //Function selection (0: I/O mode; 1: peripheral module function) 0000 0000
	P6DIR |= 0xCF; //Direction selection (0: Input; 1: Output) 1100 1111
	reset();
	start_transmission();
	measure_temperature();
	temp= read_temp_measurement();
	//debug_printf("Current temperature: %d\r\n",temp);
	return temp;
}

uint16_t measure_hum()
{
	uint16_t hum;
	/*Humidity acquisition*/
	P6SEL &= 0x00; //Function selection (0: I/O mode; 1: peripheral module function) 0000 0000
	P6DIR |= 0xCF; //Direction selection (0: Input; 1: Output) 1100 1111
	reset();
	start_transmission();
	measure_humidity();
	hum = read_humidity_measurement();
	//debug_printf("Current humidity: %d\r\n",hum);
	return hum;
}


